package love.shop.web.order.dto;

import lombok.Data;
import love.shop.domain.order.Order;
import love.shop.domain.order.OrderStatus;

import java.time.LocalDateTime;

@Data
public class OrdersResponseDto {

    private Long orderId;
    private String memberName;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;

    public OrdersResponseDto(Order order) {
        this.orderId = order.getId();
        this.memberName = order.getMember().getName();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();

    }

}
