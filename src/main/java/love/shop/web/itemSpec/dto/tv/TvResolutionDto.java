package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.web.item.spec.tv.TvResolution;

@Data
@NoArgsConstructor
public class TvResolutionDto {
    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    public TvResolutionDto(TvResolution tvResolution) {
        this.id = tvResolution.getId();
        this.specName = tvResolution.getSpecName();
        this.isPopularSpec = tvResolution.getIsPopularSpec();
    }
}
