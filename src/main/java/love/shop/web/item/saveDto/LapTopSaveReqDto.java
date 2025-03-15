package love.shop.web.item.saveDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import love.shop.web.item.filter.lapTop.*;

import java.util.List;


@JsonTypeName(value = "LapTop")
@EqualsAndHashCode(callSuper = true)
@Data
public class LapTopSaveReqDto extends ItemSaveReqDto {

    @NotNull
    private LapTopBrand lapTopBrand;
    @NotNull
    private LapTopCpu lapTopCpu;
    @NotNull
    private LapTopStorage lapTopStorage;
    @NotNull
    private LapTopScreenSize lapTopScreenSize;
    @NotNull
    private LapTopManufactureBrand lapTopManufactureBrand;

    public LapTopSaveReqDto(String name, int price, int stockQuantity, List<Integer> categoriesId, String dataType, LapTopBrand lapTopBrand, LapTopCpu lapTopCpu, LapTopStorage lapTopStorage, LapTopScreenSize lapTopScreenSize, LapTopManufactureBrand lapTopManufactureBrand) {
        super(name, price, stockQuantity, categoriesId, dataType);
        this.lapTopBrand = lapTopBrand;
        this.lapTopCpu = lapTopCpu;
        this.lapTopStorage = lapTopStorage;
        this.lapTopScreenSize = lapTopScreenSize;
        this.lapTopManufactureBrand = lapTopManufactureBrand;
    }
}
