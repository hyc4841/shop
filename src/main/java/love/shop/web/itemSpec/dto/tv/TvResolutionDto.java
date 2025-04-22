package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import love.shop.web.item.spec.tv.TvResolution;

@Data
public class TvResolutionDto {
    private Long id;
    private String specName;

    public TvResolutionDto(TvResolution tvResolution) {
        this.id = tvResolution.getId();
        this.specName = tvResolution.getSpecName();
    }
}
