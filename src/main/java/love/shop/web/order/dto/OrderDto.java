package love.shop.web.order.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.delivery.Delivery;
import love.shop.domain.order.Order;
import love.shop.domain.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
public class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus status;

    private DeliveryDto delivery;

    private List<OrderItemDto> orderItems;


    public OrderDto(Order order) {
        this.orderId = order.getId();
        this.name = order.getMember().getName();
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus();
        this.delivery = new DeliveryDto(order.getDelivery());
        // 이 부분이 지연로딩
        this.orderItems = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemDto(orderItem))
                .collect(Collectors.toList());
    }
}
