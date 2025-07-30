package love.shop.web.itemSpec.dto.lapTop;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.lapTop.LapTopBrand;

@Data
@NoArgsConstructor
public class LapTopBrandDto {

    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    public LapTopBrandDto(LapTopBrand lapTopBrand) {
        this.id = lapTopBrand.getId();
        this.specName = lapTopBrand.getSpecName();
        this.isPopularSpec = lapTopBrand.getIsPopularSpec();
    }
}
