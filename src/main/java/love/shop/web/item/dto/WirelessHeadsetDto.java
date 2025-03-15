package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.WirelessHeadset;

@EqualsAndHashCode(callSuper = true)
@Data
public class WirelessHeadsetDto extends ItemDto {
    public WirelessHeadsetDto(WirelessHeadset item) {
    }
}
