package love.shop.web.order.dto.payment;

import lombok.Data;

import java.util.List;

@Data
public class PaymentCustomData {

    private List<PaymentItemData> orderItemSets;

}