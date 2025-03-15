package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.Projector;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectorDto extends ItemDto {
    public ProjectorDto(Projector item) {
    }
}
