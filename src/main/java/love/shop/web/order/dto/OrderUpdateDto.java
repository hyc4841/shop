package love.shop.web.order.dto;

import lombok.Data;
import love.shop.domain.delivery.DeliveryStatus;

@Data
public class OrderUpdateDto {

    private DeliveryStatus deliveryStatus; // 배송상태

    private Long addressId;

    private String city;
    private String street;
    private String zipcode;
    private String detailedAddress;

}
