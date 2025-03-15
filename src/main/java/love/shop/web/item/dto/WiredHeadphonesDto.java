package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.WiredHeadphones;

@EqualsAndHashCode(callSuper = true)
@Data
public class WiredHeadphonesDto extends ItemDto {
    public WiredHeadphonesDto(WiredHeadphones item) {
    }
}
