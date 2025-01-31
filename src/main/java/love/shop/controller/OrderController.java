package love.shop.controller;

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
    public ResponseEntity<List<OrdersResponseDto>> findAllOrders() {
        List<Order> allOrder = orderService.findAllOrder();

        List<OrdersResponseDto> result = allOrder.stream()
                .map(o -> new OrdersResponseDto(o))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
