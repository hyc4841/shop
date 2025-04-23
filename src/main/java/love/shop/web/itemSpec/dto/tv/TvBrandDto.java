package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.web.item.spec.tv.TvBrand;

@Data
@NoArgsConstructor
public class TvBrandDto {

    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    public TvBrandDto(TvBrand tvBrand) {
        this.id = tvBrand.getId();
        this.specName = tvBrand.getSpecName();
        this.isPopularSpec = tvBrand.getIsPopularSpec();
    }
}
