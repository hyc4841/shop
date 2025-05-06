package love.shop.web.order.dto.payment;

import lombok.Data;

@Data
public class PaymentId {
    private String paymentId;

    public PaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentId() {
    }
}
