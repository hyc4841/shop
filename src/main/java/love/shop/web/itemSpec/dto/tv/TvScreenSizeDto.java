package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.tv.TvScreenSize;

@Data
@NoArgsConstructor
public class TvScreenSizeDto {
    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    public TvScreenSizeDto(TvScreenSize tvScreenSize) {
        this.id = tvScreenSize.getId();
        this.specName = tvScreenSize.getSpecName();
        this.isPopularSpec = tvScreenSize.getIsPopularSpec();
    }
}
