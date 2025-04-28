package love.shop.web.order.dto;

import lombok.Data;

import java.util.Map;

@Data
public class OrderCheckoutDto {

    private Map<Long, Integer> itemAndQuantity;

}
