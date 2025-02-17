package love.shop.web.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class PhoneNumUpdateReqDto {

    @NotBlank
    @Length(min = 11, max = 11, message = "11자리 휴대전화 번호를 입력해주세요.")
    private String newPhoneNum;
}
