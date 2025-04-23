package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.web.item.spec.tv.TvScreenSize;

@Data
@NoArgsConstructor
public class TvScreenSizeDto {
    private Long id;
    private String specName;

    public TvScreenSizeDto(TvScreenSize tvScreenSize) {
        this.id = tvScreenSize.getId();
        this.specName = tvScreenSize.getSpecName();
    }
}
