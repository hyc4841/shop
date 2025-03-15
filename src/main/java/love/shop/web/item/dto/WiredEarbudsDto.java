package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.WiredEarbuds;

@EqualsAndHashCode(callSuper = true)
@Data
public class WiredEarbudsDto extends ItemDto {
    public WiredEarbudsDto(WiredEarbuds item) {
    }
}
