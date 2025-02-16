package love.shop.web.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class PhoneNumUpdateReqDto {

    @NotBlank
    @Length(min = 11, max = 11)
    private String newPhoneNum;
}
