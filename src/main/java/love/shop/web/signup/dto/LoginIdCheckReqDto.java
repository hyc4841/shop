package love.shop.web.signup.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import love.shop.domain.member.validator.ValidLoginIdBlank;

@Data
public class LoginIdCheckReqDto {
    @NotBlank(message = "아이디를 입력해주세요")
    @ValidLoginIdBlank
    private String loginId;
}
