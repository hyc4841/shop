package love.shop.web.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import love.shop.domain.member.validator.ValidEmailBlank;
import love.shop.domain.member.validator.ValidEmailDuplication;

@Data
public class EmailUpdateReqDto {

    @NotBlank
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @ValidEmailDuplication
    @ValidEmailBlank
    private String newEmail;
}
