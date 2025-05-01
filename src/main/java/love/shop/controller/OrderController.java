package love.shop.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import love.shop.common.exception.OrderMemberNotMatchException;
import love.shop.common.exception.UnauthorizedAccessException;
import love.shop.domain.item.Item;
import love.shop.domain.member.Member;
import love.shop.domain.order.Order;
import love.shop.repository.item.ItemRepository;
import love.shop.service.item.ItemService;
import love.shop.service.member.MemberService;
import love.shop.service.order.OrderService;
import love.shop.web.item.dto.ItemDto;
import love.shop.web.login.dto.CustomUser;
import love.shop.web.login.dto.MemberDto;
import love.shop.web.order.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemRepository itemRepository;
    private final ItemService itemService;

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

        Member member = memberService.findMemberById(memberId);
        MemberDto memberDto = new MemberDto(member);

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

        CheckoutOrderWrapper<Object> result = new CheckoutOrderWrapper<>(orderPreviewDtoList, memberDto);

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
