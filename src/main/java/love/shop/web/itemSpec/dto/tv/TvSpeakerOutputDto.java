package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import love.shop.web.item.spec.tv.TvSpeakerOutput;

@Data
public class TvSpeakerOutputDto {
    private Long id;
    private String specName;

    public TvSpeakerOutputDto(TvSpeakerOutput tvSpeakerOutput) {
        this.id = tvSpeakerOutput.getId();
        this.specName = tvSpeakerOutput.getSpecName();
    }
}
