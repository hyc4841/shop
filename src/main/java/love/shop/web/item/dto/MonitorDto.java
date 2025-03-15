package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.Monitor;

@EqualsAndHashCode(callSuper = true)
@Data
public class MonitorDto extends ItemDto {
    public MonitorDto(Monitor item) {
    }
}
