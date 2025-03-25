package love.shop.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import love.shop.common.exception.UnauthorizedAccessException;
import love.shop.domain.cart.Cart;
import love.shop.domain.item.Item;
import love.shop.domain.member.Member;
import love.shop.domain.order.Order;
import love.shop.repository.item.ItemRepository;
import love.shop.repository.order.OrderRepository;
import love.shop.service.member.MemberService;
import love.shop.service.order.OrderService;
import love.shop.web.cart.dto.CartSaveReqDto;
import love.shop.web.item.dto.ItemDto;
import love.shop.web.login.dto.CustomUser;
import love.shop.web.login.dto.MemberDto;
import love.shop.web.order.dto.OrderDto;
import love.shop.web.order.dto.OrderReqDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final ItemRepository itemRepository;

    // 멤버 주문 조회
    @GetMapping("/orders")
    public ResponseEntity<AllOrders<List<OrderDto>>> findOrdersByMember(@RequestParam(value = "offset", defaultValue = "0") int offset,
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

    // 주문 저장
    @PostMapping("/order")
    public ResponseEntity<OrderDto> saveOrder(@RequestBody OrderReqDto orderReqDto) {
        // 결재 방식, 결재 정보도 필요함

        // 주문 완성 조건
        // 배송 주소(멤버가 저장해놓은 주소 혹은 직접 입력한 주소), 주문 멤버, 주문 상품 + 수량,

        // 주문을 만들기 위해서 뭐가 필요하지? 멤버, 아이템, 배송정보가 필요함
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기
        // 주문한 멤버 정보는 해결

        // 배송 주소는 배송 id

        Long orderId = orderService.order(memberId, orderReqDto.getOrderItemSets(), orderReqDto.getAddressId());

        Order order = orderService.findOrderById(orderId);
        OrderDto orderDto = new OrderDto(order);

        return ResponseEntity.ok(orderDto);
    }

    // 주문 취소
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);

        // 주문을 찾은 다음에
        // 주문의 상태를 취소 상태로 바꾼다

        // 취소된 주문 정보 반환하는 걸로 바꿔야함.
        return ResponseEntity.ok("취소 완료");
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

    @Data
    @AllArgsConstructor
    static class AllOrders<T> {
        private T orders;
        private int count;
    }

}
