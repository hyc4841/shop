package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.MFP;

@EqualsAndHashCode(callSuper = true)
@Data
public class MFPDto extends ItemDto {
    public MFPDto(MFP item) {

    }
}
