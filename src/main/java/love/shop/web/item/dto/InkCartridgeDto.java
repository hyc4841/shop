package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.InkCartridge;

@EqualsAndHashCode(callSuper = true)
@Data
public class InkCartridgeDto extends ItemDto {
    public InkCartridgeDto(InkCartridge item) {
    }
}
