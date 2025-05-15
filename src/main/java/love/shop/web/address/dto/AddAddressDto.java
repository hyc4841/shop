package love.shop.web.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddAddressDto {

    @NotNull
    private Integer zipcode;
    @NotBlank
    private String city;
    @NotBlank
    private String street;
    @NotBlank
    private String detailedAddress;

    private String addressName;

}
