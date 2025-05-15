package love.shop.web.address.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteAddressDto {
    @NotNull
    private Long addressId;
}
