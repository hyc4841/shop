package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.tv.TvSpeakerOutput;

@Data
@NoArgsConstructor
public class TvSpeakerOutputDto {
    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    public TvSpeakerOutputDto(TvSpeakerOutput tvSpeakerOutput) {
        this.id = tvSpeakerOutput.getId();
        this.specName = tvSpeakerOutput.getSpecName();
        this.isPopularSpec = tvSpeakerOutput.getIsPopularSpec();
    }
}
