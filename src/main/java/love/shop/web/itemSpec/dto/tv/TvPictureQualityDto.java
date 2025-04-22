package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import love.shop.web.item.spec.tv.TvPictureQuality;

@Data
public class TvPictureQualityDto {
    private Long id;
    private String specName;

    public TvPictureQualityDto(TvPictureQuality tvPictureQuality) {
        this.id = tvPictureQuality.getId();
        this.specName = tvPictureQuality.getSpecName();
    }
}
