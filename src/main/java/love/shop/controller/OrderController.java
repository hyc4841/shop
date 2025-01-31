package love.shop.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.order.Order;
import love.shop.service.order.OrderService;
import love.shop.web.order.dto.OrdersResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<Result<List<OrdersResponseDto>>> findAllOrders() {
        List<Order> allOrder = orderService.findAllOrder();

        List<OrdersResponseDto> result = allOrder.stream()
                .map(o -> new OrdersResponseDto(o))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result<List<OrdersResponseDto>>(result, result.size()));
    }

    // 이거 고급 기술임. 만약에 클라이언트에서 모든과 주문 개수를 요청했을 때 그냥 DTO로 넘기면 배열 값으로 바로 넘어가기 때문에 필드 확장이 불가능함.
    // 클레스로 컬렉션을 감싸면 나중에 필요한 필드를 추가할 수 있음
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data; // 주문 컬렉션. 주문 리스트
        private int count; // 주문 개수
    }

}
