package love.shop.web.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class NameUpdateReqDto {

    @NotBlank
    @Length(min = 2, max = 10)
    private String newName;

}
