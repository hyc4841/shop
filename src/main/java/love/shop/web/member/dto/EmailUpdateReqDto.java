package love.shop.web.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import love.shop.domain.member.ValidEmail;

@Data
@AllArgsConstructor
public class EmailUpdateReqDto {

    @NotBlank
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @ValidEmail
    private String newEmail;
}
