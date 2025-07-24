package love.shop.domain.item.type;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.item.*;
import love.shop.domain.itemSpec.spec.lapTop.*;
import love.shop.web.item.saveDto.LapTopSaveReqDto;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LapTop extends Item {

    private String lapTopBrand;
    private String lapTopCpu;
    private String lapTopStorage;
    private String lapTopScreenSize;
    private String lapTopManufactureBrand;
    private final String dataType = "LapTop";

    public LapTop(String name, int price, int stockQuantity, String lapTopBrand, String lapTopCpu, String lapTopStorage, String lapTopScreenSize, String lapTopManufactureBrand) {
        super(name, price, stockQuantity);
        this.lapTopBrand = lapTopBrand;
        this.lapTopCpu = lapTopCpu;
        this.lapTopStorage = lapTopStorage;
        this.lapTopScreenSize = lapTopScreenSize;
        this.lapTopManufactureBrand = lapTopManufactureBrand;
    }

    public LapTop(LapTopSaveReqDto laptopDto) {
        super(laptopDto.getName(), laptopDto.getPrice(), laptopDto.getStockQuantity());
        this.lapTopBrand = laptopDto.getLapTopBrand();
        this.lapTopCpu = laptopDto.getLapTopCpu();
        this.lapTopStorage = laptopDto.getLapTopStorage();
        this.lapTopScreenSize = laptopDto.getLapTopScreenSize();
        this.lapTopManufactureBrand = laptopDto.getLapTopManufactureBrand();
    }


    @Override
    public String getType() {
        return dataType;
    }
}
