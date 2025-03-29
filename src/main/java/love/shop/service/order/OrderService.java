package love.shop.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.OrderMemberNotMatchException;
import love.shop.domain.address.Address;
import love.shop.domain.delivery.Delivery;
import love.shop.domain.delivery.DeliveryStatus;
import love.shop.domain.item.Item;
import love.shop.domain.member.Member;
import love.shop.domain.order.Order;
import love.shop.domain.orderItem.OrderItem;
import love.shop.repository.address.AddressRepository;
import love.shop.repository.item.ItemRepository;
import love.shop.repository.member.MemberRepository;
import love.shop.repository.order.OrderRepository;
import love.shop.web.order.dto.OrderItemSet;
import love.shop.web.order.dto.OrderReqDto;
import love.shop.web.order.dto.OrderUpdateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final AddressRepository addressRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, OrderReqDto orderReqDto) {
        // 주문한 멤버 조회
        Member member = memberRepository.findMemberById(memberId);

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemSet orderItemSet : orderReqDto.getOrderItemSets()) {
            Item item = itemRepository.findOne(orderItemSet.getItemId()).orElseThrow(); // 주문한 아이템 조회
            OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), orderItemSet.getCount()); // 주문 상품 생성
            orderItems.add(orderItem);
        }

        Address address = null;
        // addressId가 있고, zepcode 가 없으면 미리 저장된 주소 사용으로 간주
        if (orderReqDto.getAddressId() != null && orderReqDto.getZipcode() == null) {
            // 주문자가 저장해둔 배송지를 선택한 경우
            // 회원이 등록해놓은 주소 조회
            address = addressRepository.findAddressById(orderReqDto.getAddressId());
        } else if (orderReqDto.getAddressId() == null && orderReqDto.getZipcode() != null){
            address = new Address(orderReqDto.getCity(), orderReqDto.getStreet(), orderReqDto.getZipcode(), orderReqDto.getDetailedAddress(), member);
            address.setMember(member);
            addressRepository.save(address);
        }

        // 배송정보 생성
        Delivery delivery = new Delivery(address, DeliveryStatus.PENDING);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItems); // 현재 로직은 주문 아이템이 하나만 들어간다.

        orderRepository.save(order); // order를 persist하게 되면 order에 id가 생긴다.
        return order.getId();
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

    @Transactional
    public Order updateOrder(OrderUpdateDto orderUpdateDto) {

        orderRepository.
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
