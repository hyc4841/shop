package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.BeamScreen;

@EqualsAndHashCode(callSuper = true)
@Data
public class BeamScreenDto extends ItemDto {


    public BeamScreenDto(BeamScreen item) {
    }
}
