package love.shop.web.item.saveDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import love.shop.domain.itemSpec.spec.lapTop.*;

import java.util.List;

@JsonTypeName(value = "LapTop")
@EqualsAndHashCode(callSuper = true)
@Data
public class LapTopSaveReqDto extends ItemSaveReqDto {

    private String lapTopBrand;
    private String lapTopCpu;
    private String lapTopStorage;
    private String lapTopScreenSize;
    private String lapTopManufactureBrand;

    public LapTopSaveReqDto(String lapTopBrand, String lapTopCpu, String lapTopStorage, String lapTopScreenSize, String lapTopManufactureBrand) {
        this.lapTopBrand = lapTopBrand;
        this.lapTopCpu = lapTopCpu;
        this.lapTopStorage = lapTopStorage;
        this.lapTopScreenSize = lapTopScreenSize;
        this.lapTopManufactureBrand = lapTopManufactureBrand;
    }

    public LapTopSaveReqDto(String name, int price, int stockQuantity, List<Integer> categoriesId, String dataType, String lapTopBrand, String lapTopCpu, String lapTopStorage, String lapTopScreenSize, String lapTopManufactureBrand) {
        super(name, price, stockQuantity, categoriesId, dataType);
        this.lapTopBrand = lapTopBrand;
        this.lapTopCpu = lapTopCpu;
        this.lapTopStorage = lapTopStorage;
        this.lapTopScreenSize = lapTopScreenSize;
        this.lapTopManufactureBrand = lapTopManufactureBrand;
    }
}
