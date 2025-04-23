package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.web.item.spec.tv.TvPictureQuality;

@Data
@NoArgsConstructor
public class TvPictureQualityDto {
    private Long id;
    private String specName;

    public TvPictureQualityDto(TvPictureQuality tvPictureQuality) {
        this.id = tvPictureQuality.getId();
        this.specName = tvPictureQuality.getSpecName();
    }
}
