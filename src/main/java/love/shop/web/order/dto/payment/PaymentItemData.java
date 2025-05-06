package love.shop.web.order.dto.payment;

import lombok.Data;

@Data
public class PaymentItemData {
    private Long itemId;
    private Integer quantity;
    private Integer totalPrice;
    // 추후에는 뭐 할인율, 쿠폰, 이벤트 등등 이런거 정보에 넣어야 함.
}
