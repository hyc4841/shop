package love.shop.web.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressUpdateReqDto {

    @NotBlank
    private String newZipcode;
    @NotBlank
    private String newStreet;
    @NotBlank
    private String newCity;
    @NotBlank
    private String newDetailedAddress;

}
