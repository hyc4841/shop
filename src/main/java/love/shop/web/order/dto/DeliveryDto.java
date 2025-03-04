package love.shop.web.order.dto;

import lombok.Data;
import love.shop.domain.delivery.Delivery;
import love.shop.domain.delivery.DeliveryStatus;
import love.shop.web.address.dto.AddressDto;

@Data
public class DeliveryDto {

    private Long orderId;

    private AddressDto address;

    private DeliveryStatus status;

    public DeliveryDto(Delivery delivery) {
        this.orderId = delivery.getOrder().getId();
        this.address = new AddressDto(delivery.getAddress());
        this.status = delivery.getStatus();
    }
}
