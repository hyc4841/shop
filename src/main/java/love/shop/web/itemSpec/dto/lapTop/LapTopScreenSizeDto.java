package love.shop.web.itemSpec.dto.lapTop;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.lapTop.LapTopScreenSize;

@Data
@NoArgsConstructor
public class LapTopScreenSizeDto {
    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    public LapTopScreenSizeDto(LapTopScreenSize lapTopScreenSize) {
        this.id = lapTopScreenSize.getId();
        this.specName = lapTopScreenSize.getSpecName();
        this.isPopularSpec = lapTopScreenSize.getIsPopularSpec();
    }
}
