package love.shop.web.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import love.shop.domain.member.validator.ValidLoginId;
import love.shop.domain.member.validator.ValidLoginIdBlank;

@Data
public class LoginIdUpdateReqDto {

    @NotBlank
    @ValidLoginId
    @ValidLoginIdBlank
    private String newLoginId;
}
