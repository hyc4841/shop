package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.tv.TvHDR;

@Data
@NoArgsConstructor
public class TvHDRDto {
    private Long id;
    private String specName;
    private Boolean isPopularSpec;


    public TvHDRDto(TvHDR tvHDR) {
        this.id = tvHDR.getId();
        this.specName = tvHDR.getSpecName();
        this.isPopularSpec = tvHDR.getIsPopularSpec();
    }
}
