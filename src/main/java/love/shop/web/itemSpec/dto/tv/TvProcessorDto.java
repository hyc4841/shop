package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.tv.TvProcessor;

@Data
@NoArgsConstructor
public class TvProcessorDto {
    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    public TvProcessorDto(TvProcessor tvProcessor) {
        this.id = tvProcessor.getId();
        this.specName = tvProcessor.getSpecName();
        this.isPopularSpec = tvProcessor.getIsPopularSpec();
    }
}
