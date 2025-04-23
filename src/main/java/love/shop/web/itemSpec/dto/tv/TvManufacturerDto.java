package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.web.item.spec.tv.TvManufacturer;

@Data
@NoArgsConstructor
public class TvManufacturerDto {
    private Long id;
    private String specName;

    public TvManufacturerDto(TvManufacturer tvManufacturer) {
        this.id = tvManufacturer.getId();
        this.specName = tvManufacturer.getSpecName();
    }
}
