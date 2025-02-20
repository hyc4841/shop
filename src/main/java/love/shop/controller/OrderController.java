package love.shop.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.order.Order;
import love.shop.repository.order.OrderRepository;
import love.shop.service.order.OrderService;
import love.shop.web.order.dto.OrderDto;
import love.shop.web.order.dto.OrdersResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    //    @GetMapping("/orders")
    public ResponseEntity<AllOrdersResult<List<OrdersResponseDto>>> findAllOrders() {
        List<Order> allOrder = orderService.findAllOrder();

        List<OrdersResponseDto> result = allOrder.stream()
                .map(o -> new OrdersResponseDto(o))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new AllOrdersResult<List<OrdersResponseDto>>(result, result.size()));
    }

    @GetMapping("/orders")
    public ResponseEntity<AllOrders<List<OrderDto>>> findAllOrders(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                                   @RequestParam(value = "limit", defaultValue = "100") int limit) {

        log.info("여기서 일단 fetch join으로 가져오고.");
        List<Order> orders = orderService.findAllOrders(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new AllOrders<List<OrderDto>>(result, result.size()));
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


    @GetMapping("/orders/test1")
    public ResponseEntity<List<OrderDto>> findOrdersByMemberId() {
        Long memberId = 1L;
        List<Order> orders = orderRepository.findOrdersByMemberId(1L);

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

        List<Order> orders = orderRepository.findAllOrdersV2(0, 100);

        List<OrderDto> list = new ArrayList<>();

        for (Order order : orders) {
            OrderDto orderDto = new OrderDto(order);
            list.add(orderDto);
        }

        return ResponseEntity.ok(list);
    }











}
