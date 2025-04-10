package love.shop.web.signup.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailCertificationDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
//    @ValidEmailDuplication 이거 다시 원상 복구 시켜야함. 지금은 테스트라 주석처리함
    private String email;

}
