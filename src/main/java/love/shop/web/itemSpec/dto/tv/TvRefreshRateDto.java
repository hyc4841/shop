package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.tv.TvRefreshRate;

@Data
@NoArgsConstructor
public class TvRefreshRateDto {
    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    public TvRefreshRateDto(TvRefreshRate tvRefreshRate) {
        this.id = tvRefreshRate.getId();
        this.specName = tvRefreshRate.getSpecName();
        this.isPopularSpec = tvRefreshRate.getIsPopularSpec();
    }
}
