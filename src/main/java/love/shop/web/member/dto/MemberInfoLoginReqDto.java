package love.shop.web.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import love.shop.domain.member.ValidPasswordCheck;

@Data
@AllArgsConstructor
public class MemberInfoLoginReqDto {

    @NotBlank
    @ValidPasswordCheck
    private String password;

}
