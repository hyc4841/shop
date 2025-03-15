package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.WirelessEarbuds;

@EqualsAndHashCode(callSuper = true)
@Data
public class WirelessEarbudsDto extends ItemDto {
    public WirelessEarbudsDto(WirelessEarbuds item) {

    }
}
