package love.shop.web.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import love.shop.domain.member.ValidEmail;

@Data
@RequiredArgsConstructor
public class EmailUpdateReqDto {

    @NotBlank
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @ValidEmail
    private String newEmail;
}
