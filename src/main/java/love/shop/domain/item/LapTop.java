package love.shop.domain.item;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public LapTop(LapTop lapTop) {
        super(lapTop.getName(), lapTop.getPrice(), lapTop.getStockQuantity());
        this.lapTopBrand = lapTop.getLapTopBrand();
        this.lapTopCpu = lapTop.getLapTopCpu();
        this.lapTopStorage = lapTop.getLapTopStorage();
        this.lapTopScreenSize = lapTop.getLapTopScreenSize();
        this.lapTopManufactureBrand = lapTop.getLapTopManufactureBrand();
    }

}
