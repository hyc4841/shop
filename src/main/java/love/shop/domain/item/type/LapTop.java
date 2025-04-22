package love.shop.domain.item.type;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.item.*;
import love.shop.web.item.spec.lapTop.*;
import love.shop.web.item.saveDto.LapTopSaveReqDto;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LapTop extends Item {

    @Enumerated(EnumType.STRING)
    private LapTopBrand lapTopBrand;
    @Enumerated(EnumType.STRING)
    private LapTopCpu lapTopCpu;
    @Enumerated(EnumType.STRING)
    private LapTopStorage lapTopStorage;
    @Enumerated(EnumType.STRING)
    private LapTopScreenSize lapTopScreenSize;
    @Enumerated(EnumType.STRING)
    private LapTopManufactureBrand lapTopManufactureBrand;

    private final String dataType = "LapTop";

    public LapTop(String name, int price, int stockQuantity, LapTopBrand lapTopBrand, LapTopCpu lapTopCpu, LapTopStorage lapTopStorage, LapTopScreenSize lapTopScreenSize, LapTopManufactureBrand lapTopManufactureBrand) {
        super(name, price, stockQuantity);
        this.lapTopBrand = lapTopBrand;
        this.lapTopCpu = lapTopCpu;
        this.lapTopStorage = lapTopStorage;
        this.lapTopScreenSize = lapTopScreenSize;
        this.lapTopManufactureBrand = lapTopManufactureBrand;
    }

    public LapTop(LapTopSaveReqDto lapTopDto) {
        super(lapTopDto.getName(), lapTopDto.getPrice(), lapTopDto.getStockQuantity());
        this.lapTopBrand = lapTopDto.getLapTopBrand();
        this.lapTopCpu = lapTopDto.getLapTopCpu();
        this.lapTopStorage = lapTopDto.getLapTopStorage();
        this.lapTopScreenSize = lapTopDto.getLapTopScreenSize();
        this.lapTopManufactureBrand = lapTopDto.getLapTopManufactureBrand();
    }

    @Override
    public String getType() {
        return dataType;
    }
}
