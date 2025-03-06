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

    @Override
    public String getType() {
        return dataType;
    }
}
