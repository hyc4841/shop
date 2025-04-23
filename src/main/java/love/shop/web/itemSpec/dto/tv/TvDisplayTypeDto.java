package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.web.item.spec.tv.TvDisplayType;

@Data
@NoArgsConstructor
public class TvDisplayTypeDto {
    private Long id;
    private String specName;

    public TvDisplayTypeDto(TvDisplayType tvDisplayType) {
        this.id = tvDisplayType.getId();
        this.specName = tvDisplayType.getSpecName();
    }
}
