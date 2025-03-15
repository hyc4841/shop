package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.TonerCartridge;

@EqualsAndHashCode(callSuper = true)
@Data
public class TonerCartridgeDto extends ItemDto {
    public TonerCartridgeDto(TonerCartridge item) {
    }
}
