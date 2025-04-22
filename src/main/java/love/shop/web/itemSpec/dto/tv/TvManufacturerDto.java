package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import love.shop.web.item.spec.tv.TvManufacturer;

@Data
public class TvManufacturerDto {
    private Long id;
    private String specName;

    public TvManufacturerDto(TvManufacturer tvManufacturer) {
        this.id = tvManufacturer.getId();
        this.specName = tvManufacturer.getSpecName();
    }
}
