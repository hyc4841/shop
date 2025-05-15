package love.shop.web.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressUpdateReqDto {

    @NotBlank(message = "우편주소를 입력해주세요")
    private String newZipcode;
    @NotBlank(message = "주소를 입력해주세요")
    private String newStreet;
    @NotBlank
    private String newCity;
    @NotBlank(message = "상세 주소를 입력해주세요.")
    private String newDetailedAddress;

    private String addressName;

}
