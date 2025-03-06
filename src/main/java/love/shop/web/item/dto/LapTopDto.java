package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.LapTop;

@EqualsAndHashCode(callSuper = true)
@Data
public class LapTopDto extends ItemDto {

    private String lapTopBrand;
    private String lapTopCpu;
    private String lapTopStorage;
    private String lapTopScreenSize;
    private String lapTopManufactureBrand;

    public LapTopDto(LapTop lapTop) {
        super(lapTop.getName(), lapTop.getPrice(), lapTop.getStockQuantity());
        this.lapTopBrand = String.valueOf(lapTop.getLapTopBrand());
        this.lapTopCpu = String.valueOf(lapTop.getLapTopCpu());
        this.lapTopStorage = String.valueOf(lapTop.getLapTopStorage());
        this.lapTopScreenSize = String.valueOf(lapTop.getLapTopScreenSize());
        this.lapTopManufactureBrand = String.valueOf(lapTop.getLapTopManufactureBrand());
    }

}

// 일단 사용자가 선택한 카테고리에 해당하는 아이템 조회까지 성공함