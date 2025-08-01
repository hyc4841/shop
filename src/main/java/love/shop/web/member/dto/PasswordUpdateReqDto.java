package love.shop.web.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import love.shop.domain.member.validator.ValidPasswordCheck;

@Data
@AllArgsConstructor
public class PasswordUpdateReqDto {

    @NotBlank
    @ValidPasswordCheck // 현재 유저의 비밀번호와 유저가 입력한 비밀번호가 맞는지 검증
    private String curPwd;

    @NotBlank
    private String newPwd;
    @NotBlank
    private String newPwdCon;

}
