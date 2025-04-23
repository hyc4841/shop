package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.web.item.spec.tv.TvProcessor;

@Data
@NoArgsConstructor
public class TvProcessorDto {
    private Long id;
    private String specName;

    public TvProcessorDto(TvProcessor tvProcessor) {
        this.id = tvProcessor.getId();
        this.specName = tvProcessor.getSpecName();
    }
}
