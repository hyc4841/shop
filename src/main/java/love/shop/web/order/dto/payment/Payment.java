package love.shop.web.order.dto.payment;

import lombok.Data;

@Data
public class Payment {
    private String status;

    public Payment(String status) {
        this.status = status;
    }

    public Payment() {
    }
}
