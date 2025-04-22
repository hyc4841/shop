package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import love.shop.web.item.spec.tv.TvHDR;

@Data
public class TvHDRDto {
    private Long id;
    private String specName;

    public TvHDRDto(TvHDR tvHDR) {
        this.id = tvHDR.getId();
        this.specName = tvHDR.getSpecName();
    }
}
