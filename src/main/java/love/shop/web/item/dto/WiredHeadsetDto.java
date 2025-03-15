package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.WiredHeadset;

@EqualsAndHashCode(callSuper = true)
@Data
public class WiredHeadsetDto extends ItemDto {
    public WiredHeadsetDto(WiredHeadset item) {
    }
}
