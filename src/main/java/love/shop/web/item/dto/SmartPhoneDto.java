package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.SmartPhone;

@EqualsAndHashCode(callSuper = true)
@Data
public class SmartPhoneDto extends ItemDto {


    public SmartPhoneDto(SmartPhone item) {
    }
}
