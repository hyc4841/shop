package love.shop.web.order.dto;

import lombok.Data;
import love.shop.domain.orderItem.OrderItem;

@Data
public class OrderItemDto {

    // 엔티티는 절대로 응답 데이터로 노출 시켜선 안된다. 이렇게 DTO를 만들어서 제공 해야함.

    private String itemName;    // 상품 이름
    private int orderPrice;     // 주문 가격
    private int count;          // 주문 수량

    public OrderItemDto(OrderItem orderItem) {
        this.itemName = orderItem.getItem().getName();
        this.orderPrice = orderItem.getOrderPrice();
        this.count = orderItem.getCount();
    }
}
