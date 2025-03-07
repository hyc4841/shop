package love.shop.domain.item.type;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.item.*;
import love.shop.web.item.dto.LapTopSaveReqDto;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LapTop extends Item {

    private LapTopBrand lapTopBrand;
    private LapTopCpu lapTopCpu;
    private LapTopStorage lapTopStorage;
    private LapTopScreenSize lapTopScreenSize;
    private LapTopManufactureBrand lapTopManufactureBrand;

    private String dataType;


    public LapTop(String name, int price, int stockQuantity, LapTopBrand lapTopBrand, LapTopCpu lapTopCpu, LapTopStorage lapTopStorage, LapTopScreenSize lapTopScreenSize, LapTopManufactureBrand lapTopManufactureBrand) {
        super(name, price, stockQuantity);
        this.lapTopBrand = lapTopBrand;
        this.lapTopCpu = lapTopCpu;
        this.lapTopStorage = lapTopStorage;
        this.lapTopScreenSize = lapTopScreenSize;
        this.lapTopManufactureBrand = lapTopManufactureBrand;
        this.dataType = "LapTop";
    }

    public LapTop(LapTopSaveReqDto lapTopDto) {
        super(lapTopDto.getName(), lapTopDto.getPrice(), lapTopDto.getStockQuantity());
        this.lapTopBrand = lapTopDto.getLapTopBrand();
        this.lapTopCpu = lapTopDto.getLapTopCpu();
        this.lapTopStorage = lapTopDto.getLapTopStorage();
        this.lapTopScreenSize = lapTopDto.getLapTopScreenSize();
        this.lapTopManufactureBrand = lapTopDto.getLapTopManufactureBrand();
        this.dataType = "LapTop";
    }

    @Override
    public String getType() {
        return dataType;
    }
}
