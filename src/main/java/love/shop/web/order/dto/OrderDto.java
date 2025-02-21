package love.shop.web.order.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
    private OrderStatus orderStatus;

    private List<OrderItemDto> orderItems;
    private String city;
    private String street;
    private String detailedAddress;
    private String zipcode;

    public OrderDto(Order order) {
        this.orderId = order.getId();
        this.name = order.getMember().getName();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.city = order.getDelivery().getCity();
        this.street = order.getDelivery().getStreet();
        this.detailedAddress = order.getDelivery().getDetailedAddress();
        this.zipcode = order.getDelivery().getZipcode();
        log.info("OrderItem 지연로딩 하나?");
        this.orderItems = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemDto(orderItem))
                .collect(Collectors.toList());
    }
}
