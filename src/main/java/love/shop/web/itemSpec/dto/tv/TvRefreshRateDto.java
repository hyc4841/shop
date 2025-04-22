package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import love.shop.web.item.spec.tv.TvRefreshRate;

@Data
public class TvRefreshRateDto {
    private Long id;
    private String specName;

    public TvRefreshRateDto(TvRefreshRate tvRefreshRate) {
        this.id = tvRefreshRate.getId();
        this.specName = tvRefreshRate.getSpecName();
    }
}
