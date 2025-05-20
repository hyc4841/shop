package love.shop.web.order.dto.payment;

import lombok.Data;

@Data
public class PaymentCompleteDto {
    private String paymentId;
    private Long orderId;
}
