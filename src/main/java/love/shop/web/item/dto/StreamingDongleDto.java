package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.StreamingDongle;

@EqualsAndHashCode(callSuper = true)
@Data
public class StreamingDongleDto extends ItemDto {
    public StreamingDongleDto(StreamingDongle item) {
    }
}
