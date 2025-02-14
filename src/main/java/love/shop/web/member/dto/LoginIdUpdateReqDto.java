package love.shop.web.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import love.shop.domain.member.ValidLoginId;

@Data
@AllArgsConstructor
public class LoginIdUpdateReqDto {

    @NotBlank
    @ValidLoginId
    private String newLoginId;
}
