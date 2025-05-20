package love.shop.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.portone.sdk.server.payment.PaidPayment;
import io.portone.sdk.server.payment.PaymentClient;
import io.portone.sdk.server.payment.VirtualAccountIssuedPayment;
import io.portone.sdk.server.webhook.WebhookVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.OrderMemberNotMatchException;
import love.shop.common.exception.SalesPageNotExistException;
import love.shop.common.exception.SyncPaymentException;
import love.shop.common.exception.UserNotExistException;
import love.shop.domain.address.Address;
import love.shop.domain.delivery.Delivery;
import love.shop.domain.delivery.DeliveryStatus;
import love.shop.domain.item.Item;
import love.shop.domain.member.Member;
import love.shop.domain.order.Order;
import love.shop.domain.order.OrderStatus;
import love.shop.domain.orderItem.OrderItem;
import love.shop.domain.salesPage.SalesPage;
import love.shop.repository.ItemPage.SalesPageRepository;
import love.shop.repository.address.AddressRepository;
import love.shop.repository.item.ItemRepository;
import love.shop.repository.member.MemberRepository;
import love.shop.repository.order.OrderRepository;
import love.shop.web.order.dto.OrderItemSet;
import love.shop.web.order.dto.OrderReqDto;
import love.shop.web.order.dto.OrderDeliveryAddressUpdateDto;
import love.shop.web.order.dto.payment.PaymentCustomData;
import love.shop.web.order.dto.payment.PaymentItemData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final AddressRepository addressRepository;
    private final SalesPageRepository salesPageRepository;

    private final PaymentClient portone;
    private final WebhookVerifier portoneWebHook;

    @Transactional
    public Order syncPayment(String paymentId, Long orderId) throws ExecutionException, InterruptedException {
        log.info("결제 아이디 확인={}", paymentId);

        // 데이터베이스에 저장한 결제 정보 가져오기
        Order order = orderRepository.findOne(orderId);

        try {
            io.portone.sdk.server.payment.Payment actualPayment = portone.getPayment(paymentId).get();

            log.info("actualPayment={}", actualPayment);
            if (actualPayment instanceof PaidPayment paidPayment) {
                // 클라이언트에서 넘어온 결제 데이터와 서버에 저장된 상품 데이터 비교
                if (!verifyPayment(paidPayment)) throw new SyncPaymentException("400", "잘못됨"); // 예외 발생

                log.info("결제 성공 {}", actualPayment);
                order.setStatus(OrderStatus.PAYMENT_COMPLETE);
            } else if (actualPayment instanceof VirtualAccountIssuedPayment) {
                //  VirtualAccountIssuedPayment 가상 계좌 이슈로 예외 발생?
                order.setStatus(OrderStatus.CANCELLED);
                return order;
            }
            return order;

        } catch (RuntimeException e) {
            throw new SyncPaymentException("400", "잘못됨");
        }
    }

    // 포트원 결제 정보와 서버 결제 정보가 일치하는지 확인
    private boolean verifyPayment(PaidPayment payment) {
        // 포트원으로 보내는 결제 데이터에 customData를 넣을 수 있음. 거기에 구매하는 상품 정보를 넣으면 될듯.
        // 서버가 가지고 있는 상품에 대한 가격 정보 등이 정확한지 이 메서드에서 판단한다.
        String customData = payment.getCustomData();

        log.info("customData={}", customData);

        if (customData == null) {
            log.info("결제 정보에 상품 정보가 없음");
            // 검증을 할 수 없을 때, 결제를 그냥 취소해버리자
            portone.cancelPayment(payment.getId(), null, null, null, "결제 검증 불가",
                    null, null, null);
            return false;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        // 결제 정보에 있던 customData를 서버가 가지고 있는 데이터 클래스로 파싱하기
        PaymentCustomData paymentData = null;
        try {
            paymentData = objectMapper.readValue(customData, PaymentCustomData.class);
            log.info("결제 정보 파싱={}", paymentData);
        } catch (JsonProcessingException e) {
            log.error("결제 정보 파싱 불가", e);
            portone.cancelPayment(payment.getId(), null, null, null, "결제 검증 불가",
                    null, null, null);
            return false;
        }

        for (PaymentItemData itemData : paymentData.getOrderItemSets()) {
            Item item = itemRepository.findOne(itemData.getItemId()).orElseThrow(() -> new RuntimeException());
            int price = item.getPrice(); // 서버에 저장된 상품 가격
            if (itemData.getTotalPrice() != price * itemData.getQuantity()) {
//                throw new RuntimeException("결제 정보와 서버에 저장된 데이터가 다름. 데이터 조작이 의심됨!");
                portone.cancelPayment(payment.getId(), null, null, null, "데이터 조작 의심",
                        null, null, null);
                return false;
            }
        }
        log.info("결제 검증 성공");
        return true;
    }

    @Transactional
    public Order paymentComplete(Long orderId, Long memberId) {
        Order order = orderRepository.findOne(orderId);

        // 주문 멤버와 로그인 멤버가 일치하는지 일단 확인
        if (Objects.equals(order.getMember().getId(), memberId)) {
            order.setStatus(OrderStatus.PAYMENT_COMPLETE); // 더티체킹으로 변경
        }

        return order;
    }

    // 주문
    @Transactional
    public Order order(Long memberId, OrderReqDto orderReqDto) {
        SalesPage salesPage = salesPageRepository.findPageByPageId(orderReqDto.getSalesPageId()).orElseThrow(() -> new SalesPageNotExistException());
        // 주문한 멤버 조회
        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new UserNotExistException());

        List<OrderItem> orderItems = new ArrayList<>();

        // 주문 상품 뽑아내기
        for (OrderItemSet orderItemSet : orderReqDto.getOrderItemSets()) {
            Item item = itemRepository.findOne(orderItemSet.getItemId()).orElseThrow(); // 주문한 아이템 조회
            OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), orderItemSet.getQuantity()); // 주문 상품 생성
            orderItems.add(orderItem);
        }

        Address address = null;
        // addressId가 있고, zipcode 가 없으면 미리 저장된 주소 사용으로 간주
        if (orderReqDto.getAddressId() != null && orderReqDto.getZipcode() == null) {
            // 주문자가 저장해둔 배송지를 선택한 경우
            // 회원이 등록해놓은 주소 조회
            address = addressRepository.findAddressById(orderReqDto.getAddressId());
        } else if (orderReqDto.getAddressId() == null && orderReqDto.getZipcode() != null){
            address = new Address(orderReqDto.getCity(), orderReqDto.getStreet(), orderReqDto.getZipcode(),
                    orderReqDto.getDetailedAddress(), member, null);
            address.setMember(member);
            addressRepository.save(address);
        }

        // 배송정보 생성
        Delivery delivery = new Delivery(address, DeliveryStatus.LABEL_CREATED); // 실제 쇼핑몰에선 송장이 나와야이 단계로 넘어가는듯?

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItems, salesPage); // 현재 로직은 주문 아이템이 하나만 들어간다.

        orderRepository.save(order); // order를 persist하게 되면 order에 id가 생긴다.
        return order;
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId, Long memberId) {
        // 주문 조회
        Order order = orderRepository.findOne(orderId);

        if (Objects.equals(order.getMember().getId(), memberId)) {
            // 주문 취소
            order.cancel();
        } else {
            throw new OrderMemberNotMatchException();
        }
    }

    // 주문 배송지 변경. 만약 아직 발송을 안했다면
    @Transactional
    public void updateOrderDeliveryAddressById(Delivery delivery, Long addressId) {
        Address address = addressRepository.findAddressById(addressId); // 바꾸려고 하는 주소
        delivery.updateAddress(address);
    }

    @Transactional
    public void updateOrderDeliveryAddressByNewAddress(Delivery delivery, OrderDeliveryAddressUpdateDto orderUpdateDto, Long memberId) {
        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new UserNotExistException());

        // 새로운 주소지 자체 생성
        Address address = new Address(orderUpdateDto.getCity(), orderUpdateDto.getStreet(),
                orderUpdateDto.getZipcode(), orderUpdateDto.getDetailedAddress(), member, null);
        // 주소 저장
        addressRepository.save(address);
        log.info("새로 저장한 주소={}", address.getId());

        delivery.updateAddress(address);
    }

    // 모든 주문 조회
    public List<Order> findAllOrder() {
        return orderRepository.findAllWithMemberDelivery();
    }

    public List<Order> findAllOrders(int offset, int limit) {
        return orderRepository.findAllWithMemberDelivery(offset, limit);
    }

    public List<Order> findOrdersByMemberId(Long memberId, int offset, int limit) {
        return orderRepository.findOrdersByMemberId(memberId, offset, limit);
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findOne(orderId);
    }



}
