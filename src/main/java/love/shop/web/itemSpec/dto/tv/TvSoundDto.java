package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.tv.TvSound;

@Data
@NoArgsConstructor
public class TvSoundDto {
    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    public TvSoundDto(TvSound tvSound) {
        this.id = tvSound.getId();
        this.specName = tvSound.getSpecName();
        this.isPopularSpec = tvSound.getIsPopularSpec();
    }
}
