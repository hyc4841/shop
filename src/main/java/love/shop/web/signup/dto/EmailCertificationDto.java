package love.shop.web.signup.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import love.shop.domain.member.validator.ValidEmailDuplication;

@Data
public class EmailCertificationDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
//    @ValidEmailDuplication
    private String email;

}
