package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class LapTopSaveReqDto extends ItemSaveReqDto {

    private LapTopBrand lapTopBrand;
    private LapTopCpu lapTopCpu;
    private LapTopStorage lapTopStorage;
    private LapTopScreenSize lapTopScreenSize;
    private LapTopManufactureBrand lapTopManufactureBrand;

    private String dataType;

    @Override
    public String getType() {
        return dataType;
    }
}
