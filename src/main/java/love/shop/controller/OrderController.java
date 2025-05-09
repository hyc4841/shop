package love.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.portone.sdk.server.payment.PaymentClient;
import io.portone.sdk.server.payment.PaidPayment;
import io.portone.sdk.server.payment.VirtualAccountIssuedPayment;
import io.portone.sdk.server.webhook.WebhookVerifier;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.OrderMemberNotMatchException;
import love.shop.common.exception.SyncPaymentException;
import love.shop.common.exception.UnauthorizedAccessException;
import love.shop.domain.address.Address;
import love.shop.domain.item.Item;
import love.shop.domain.member.Member;
import love.shop.domain.order.Order;
import love.shop.repository.item.ItemRepository;
import love.shop.service.Address.AddressService;
import love.shop.service.item.ItemService;
import love.shop.service.member.MemberService;
import love.shop.service.order.OrderService;
import love.shop.web.address.dto.AddressDto;
import love.shop.web.item.dto.ItemDto;
import love.shop.web.login.dto.CustomUser;
import love.shop.web.login.dto.MemberDto;
import love.shop.web.order.dto.*;
import love.shop.web.order.dto.payment.Payment;
import love.shop.web.order.dto.payment.PaymentCustomData;
import love.shop.web.order.dto.payment.PaymentId;
import love.shop.web.order.dto.payment.PaymentItemData;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final AddressService addressService;

    private final PaymentClient portone;
    private final WebhookVerifier portoneWebHook;

    // 클라이언트에서 결제 후 서버에서 결제 검증 컨트롤러
    @PostMapping("/order/payment/complete")
    public ResponseEntity<?> paymentComplete(@RequestBody PaymentId paymentId) {
        return ResponseEntity.ok(paymentComplete(paymentId));
    }

    private Payment syncPayment(String paymentId) throws ExecutionException, InterruptedException {
        log.info("결제 아이디 확인={}", paymentId);

        // 데이터베이스에 저장한 결제 정보 가져오기
        Payment payment = new Payment("PENDING");

        try {
            io.portone.sdk.server.payment.Payment actualPayment = portone.getPayment(paymentId).get();

            log.info("actualPayment={}", actualPayment);


            if (actualPayment instanceof PaidPayment paidPayment) {
                if (!verifyPayment(paidPayment)) throw new SyncPaymentException("400", "잘못됨"); // 예외 발생

                log.info("결제 성공 {}", actualPayment);

            } else if (actualPayment instanceof VirtualAccountIssuedPayment) {
                Payment newPayment = new Payment("VIRTUAL_ACCOUNT_ISSUED");
//                        paymentStore.put(paymentId, newPayment);
                return newPayment;
            } else {
                return payment;
            }
            return payment;

        } catch (RuntimeException e) {
            throw new SyncPaymentException("400", "잘못됨");
        }

        /*
        return Mono.fromFuture(portone.getPayment(paymentId))
                .onErrorMap(ignored -> new SyncPaymentException("400", "잘못됨"))
                .flatMap(actualPayment -> {
                    log.info("오류 발생");
                    return Mono.error(new SyncPaymentException("400", "잘못됨")); // 예외 발생
                });

         */
                /*
                .flatMap(actualPayment -> {
                    if (actualPayment instanceof PaidPayment paidPayment) {
                        if (!verifyPayment(paidPayment)) return Mono.error(new SyncPaymentException()); // 예외 발생

                        log.info("결제 성공 {}", actualPayment);

                    } else if (actualPayment instanceof VirtualAccountIssuedPayment) {
                        Payment newPayment = new Payment("VIRTUAL_ACCOUNT_ISSUED");
//                        paymentStore.put(paymentId, newPayment);
                        return Mono.just(newPayment);
                    } else {
                        return Mono.just(payment);
                    }
                    return Mono.just(payment);
                });

                 */

    }

    // 포트원 결제 정보와 서버 결제 정보가 일치하는지 확인
    private boolean verifyPayment(PaidPayment payment) {
        // 포트원으로 보내는 결제 데이터에 customData를 넣을 수 있음. 거기에 구매하는 상품 정보를 넣으면 될듯.
        // 서버가 가지고 있는 상품에 대한 가격 정보 등이 정확한지 이 메서드에서 판단한다.
        String customData = payment.getCustomData();

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

        // 결제 정보에 넣어야할 상품 정보
        // 상품 id, 상품 가격, 구매 개수 => 이것이 리스트로 들어옴.
        // 한번 결제할 때, 여러개의 상품을 한번에 결제할 수 있도록 했기 때문에.
        // 이 부분에서 브라우저에서 결제한 정보와 서버가 가지고 있는 정보가 맞는지 확인(제일 중요한건 결제 금액이 맞는지 확인)
        /*
        // 클라이언트 쪽에서 보내온 구매 상품 데이터 형식11
        [
            {
                itemId : 1,
                quantity : 1,
                totalPrice: item.price * quantity
            },
            {
                itemId : 2,
                quantity : 2,
                totalPrice: item.price * quantity
            },
            {
                itemId : 3,
                quantity : 3,
                totalPrice: item.price * quantity
            },
        ]
         */
        for (PaymentItemData itemData : paymentData.getItemData()) {
            Item item = itemService.findOne(itemData.getItemId());
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


    // 원래는 get이 맞는데, 보내오는 데이터의 형식상 post를 사용하는게 더 적합하다. 이거 트러블 슈팅거리?
    // get인데 json을 써야할때 어떻게 해야하는가에 대한 고민.
    @PostMapping("/checkout/order")
    public ResponseEntity<?> checkoutOrder(@RequestBody @Valid OrderCheckoutDto checkoutDto) {

        // 선택지가 있음
        // 프론트에서 아이템 id만 보내서 수량 괜찮은지만 보고 따로 item에 대한 데이터는 보내지 않는다. 왜냐? 이미 프론트에서 주문하려는 상품 데이터를
        // 가지고 이동했기 때문
        // 아니면 지금거 유지
        // 지금거 유지할거면 쿼리 몇개 나가는지, 쓸데없는 쿼리 나가는지 확인해야함.


        log.info("데이터 잘 오나?={}", checkoutDto);
        // 또 하나, 과연 주문 확인 화면으로 갈 때, 상품하고
        // 예외처리, 만약 주문하려는 상품의 수량이 부족하면 예외 보내줘야함. 이거 구현하기 -> 일단 구현 성공

        // 현재 로그인 중인 멤버
        Long memberId = currentUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        List<Address> addressList = addressService.findAddressesByMemberId(memberId);
        List<AddressDto> addressDtoList = AddressDto.makeAddressDtoList(addressList);

        // 주문 확인 페이지에서 뿌려줄 데이터
        // 1. 주문하려는 상품과 수량 보여주기. 가격 계산은 프론트에서
        // 2. 주문하려는 회원의 배송지 보여주기

        // 주문 하려는 상품과 수량 다시 확인해서 itemDto로 만든 다음 데이터 뿌려주기
        List<OrderPreviewDto> orderPreviewDtoList = new ArrayList<>();
        checkoutDto.getItemAndQuantity().forEach((itemId, quantity) -> {
            Item item = itemService.findOne(itemId);
            // 수량 확인
            item.checkoutStock(quantity);

            ItemDto itemDto = ItemDto.createItemDto(item);

            OrderPreviewDto orderPreviewDto = new OrderPreviewDto(itemDto, quantity);
            orderPreviewDtoList.add(orderPreviewDto);
        });

        CheckoutOrderWrapper<Object> result = new CheckoutOrderWrapper<>(orderPreviewDtoList, addressDtoList);

        // 위 로직 전부 지하화 하기

        return ResponseEntity.ok(result);
    }

    @Data
    @AllArgsConstructor
    public static class CheckoutOrderWrapper<T> {
        private T itemList;
        private T Address;
    }

    // 주문 저장
    @PostMapping("/order")
    public ResponseEntity<OrderDto> saveOrder(@RequestBody OrderReqDto orderReqDto) {
        // 아이템 id 가져오는건 그대로
        log.info("주문 요청 데이터={}", orderReqDto);
        // 결재 방식, 결재 정보도 필요함
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기

        Order order = orderService.order(memberId, orderReqDto);
        OrderDto orderDto = new OrderDto(order);

        return ResponseEntity.ok(orderDto);
    }

    // 주문 상세 조회 : 이건 추후에 PathVariable로 하지말고 그냥 body에 넣어서 처리하는 것도 방법일듯.
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDto> findOrderByOrderId(@PathVariable Long orderId) {
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기
        log.info("주문 단건 조회");
        Order order = orderService.findOrderById(orderId);

        // 1차로 멤버 권한이 없느 사용자는 스프링 필터에서 걸러지고
        // 2차로 컨트롤러에 들어와서 해당 주문의 주문 멤버인지 확인
        if (!Objects.equals(order.getMember().getId(), memberId)) {
            throw new UnauthorizedAccessException("접근 권한이 없습니다.");
        }
        OrderDto orderDto = new OrderDto(order);

        return ResponseEntity.ok(orderDto);
    }

    // 주문 취소
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        // 이 자체로도 되긴 하는데, 해당 유저가 아닌데 주문취소 요청 일부러 보내서 망칠 수도 있잖아.
        // 주문 id와 해당 주문이 지금 로그인중인 유저의 주문인지 확인 후 주문 취소하자.

        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기

        orderService.cancelOrder(orderId, memberId);

        // 주문을 찾은 다음에
        // 주문의 상태를 취소 상태로 바꾼다

        // 취소된 주문 정보 반환하는 걸로 바꿔야함.
        return ResponseEntity.ok("취소 완료");
    }

    // 배송 주소 변경. 아직 배송준비중일때만 가능하도록
    @PatchMapping("/order/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody OrderDeliveryAddressUpdateDto orderUpdateDto) {
        // 배송지 혹은 배송 상태 업데이트
        log.info("주문 배송 주소 업데이트={}", orderUpdateDto);

        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기

        Order order = orderService.findOrderById(orderId);

        // 현재 로그인 멤버와 주문 멤버가 같은지 확인
        if (Objects.equals(memberId, order.getMember().getId())) {
            if (orderUpdateDto.getAddressId() != null && orderUpdateDto.getZipcode() == null) {
                // 배송지 변경을 기존에 저장된 주소로 하는 경우
                log.info("기존 배송지로 변경");
                orderService.updateOrderDeliveryAddressById(order.getDelivery(), orderUpdateDto.getAddressId());
            } else if (orderUpdateDto.getAddressId() == null && orderUpdateDto.getZipcode() != null) {
                // 배송지 변경을 새로 입력하는 경우
                log.info("새로운 배송지 저장하기");
                orderService.updateOrderDeliveryAddressByNewAddress(order.getDelivery(), orderUpdateDto, memberId);
            }
        } else {
            throw new OrderMemberNotMatchException();
        }

        return ResponseEntity.ok(new OrderDto(order));
    }

    // 멤버 주문 조회
    @GetMapping("/orders")
    public ResponseEntity<?> findOrdersByMember(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기

        List<Order> orders = orderService.findOrdersByMemberId(memberId, offset, limit);
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new AllOrders<List<OrderDto>>(result, result.size()));
    }

    // 주문 페이지에 필요한 정보
    // 장바구니, 상품 페이지에서 다이렉트로 살때 모두 가능함. 구매 상품을 리스트로 받으니까 가능
    @GetMapping("/orderInfo")
    public ResponseEntity<?> orderInfo(@RequestParam List<Long> orderItems) {
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        Member orderMember = memberService.findMemberById(memberId);
        MemberDto orderMemberDto = new MemberDto(orderMember);

        // 구매하려는 아이템
        List<Item> orderItemsList = itemRepository.findItemsByItemId(orderItems);
        List<ItemDto> orderItemDtoList = ItemDto.createItemDtoList(orderItemsList);

        orderInfoResult<Object> orderInfoResult = new orderInfoResult<>(orderMemberDto, orderItemDtoList);
        // 주문 페이지에 뿌려줄 dto 필요
        // 주문 멤버의 정보, 구매하려는 상품 정보
        // 추후에는 멤버가 저장해둔 카드의 정보도 꺼내와서 결재를 편하게 해주는 것도 필요하다.

        return ResponseEntity.ok(orderInfoResult);
    }

    @Data
    @AllArgsConstructor
    public static class orderInfoResult<T> {
        private T member;
        private T items;
    }

    // 주문 방식이 장바구니 구매가 있을 수도 있다.


    private Long currentUser(Object principal) {
        // 그런데 SecurityContextHolder로 하는거 말고도 HttpServletRequest로 하는 방법도 있음.
             log.info("현재 로그인 중인지 아닌지 검사 실행");
        try {
            if (principal.getClass() == CustomUser.class) {
                return ((CustomUser) principal).getMemberId();
            }
            throw new UnauthorizedAccessException();
        } catch (Error error) {
            throw new UnauthorizedAccessException(error);
        }
    }


    /*

    @GetMapping("/orders/test1")
    public ResponseEntity<List<OrderDto>> findOrdersByMemberId() {
        Long memberId = 1L;
        List<Order> orders = orderRepository.findOrdersByMemberId(1L, 0, 100);

        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderDto orderDto = new OrderDto(order);
            orderDtos.add(orderDto);
        }

        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("/orders/test2")
    public ResponseEntity<List<OrderDto>> findOrdersByMember() {

        List<Order> orders = orderRepository.findAllOrders(0, 100);

        List<OrderDto> list = new ArrayList<>();

        for (Order order : orders) {
            OrderDto orderDto = new OrderDto(order);
            list.add(orderDto);
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("/orders/test3")
    public ResponseEntity<List<OrderDto>> findOrdersByMember123() {

        List<Order> orders = orderRepository.findAllOrders(0, 100);

        List<OrderDto> list = new ArrayList<>();

        for (Order order : orders) {
            OrderDto orderDto = new OrderDto(order);
            list.add(orderDto);
        }

        return ResponseEntity.ok(list);
    }

    // 이거 고급 기술임. 만약에 클라이언트에서 모든과 주문 개수를 요청했을 때 그냥 DTO로 넘기면 배열 값으로 바로 넘어가기 때문에 필드 확장이 불가능함.
    // 클레스로 컬렉션을 감싸면 나중에 필요한 필드를 추가할 수 있음
    @Data
    @AllArgsConstructor
    static class AllOrdersResult<T> {
        private T data; // 주문 컬렉션. 주문 리스트
        private int count; // 주문 개수
    }
*/
    @Data
    @AllArgsConstructor
    static class AllOrders<T> {
        private T orders;
        private int count;
    }



}
