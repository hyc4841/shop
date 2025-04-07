package love.shop.web.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailCertificationConfirmDto {

    @NotBlank
    private String email;
    @NotBlank(message = "코드를 입력해주세요")
    private String code;

}
