package love.shop.web.itemSpec.dto.lapTop;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.lapTop.LapTopManufactureBrand;

@Data
@NoArgsConstructor
public class LapTopManufactureBrandDto {

    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    public LapTopManufactureBrandDto(LapTopManufactureBrand lapTopManufactureBrand) {
        this.id = lapTopManufactureBrand.getId();
        this.specName = lapTopManufactureBrand.getSpecName();
        this.isPopularSpec = lapTopManufactureBrand.getIsPopularSpec();
    }
}
