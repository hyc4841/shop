package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.WirelessHeadphones;

@EqualsAndHashCode(callSuper = true)
@Data
public class WirelessHeadphonesDto extends ItemDto {
    public WirelessHeadphonesDto(WirelessHeadphones item) {
    }
}
