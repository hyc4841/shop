package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.tv.TvSpeakerChannel;

@Data
@NoArgsConstructor
public class TvSpeakerChannelDto {
    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    public TvSpeakerChannelDto(TvSpeakerChannel tvSpeakerChannel) {
        this.id = tvSpeakerChannel.getId();
        this.specName = tvSpeakerChannel.getSpecName();
        this.isPopularSpec = tvSpeakerChannel.getIsPopularSpec();
    }
}
