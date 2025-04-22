package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import love.shop.web.item.spec.tv.TvBrand;

@Data
public class TvBrandDto {

    private Long id;
    private String specName;

    public TvBrandDto(TvBrand tvBrand) {
        this.id = tvBrand.getId();
        this.specName = tvBrand.getSpecName();
    }
}
