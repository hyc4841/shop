package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.streamingMediaPlayer;

@EqualsAndHashCode(callSuper = true)
@Data
public class StreamingMediaPlayerDto extends ItemDto {
    public StreamingMediaPlayerDto(streamingMediaPlayer item) {
    }
}
