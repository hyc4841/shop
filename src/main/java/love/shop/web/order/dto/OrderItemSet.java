package love.shop.web.order.dto;

import lombok.Data;

@Data
public class OrderItemSet {
    private Long itemId;
    private Integer quantity;
    private Integer totalPrice;
}
