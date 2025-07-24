package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.tv.TvDisplayPanel;

@Data
@NoArgsConstructor
public class TvDisplayPanelDto {
    private Long id;
    private String specName;
    private Boolean isPopularSpec;


    public TvDisplayPanelDto(TvDisplayPanel tvDisplayPanel) {
        this.id = tvDisplayPanel.getId();
        this.specName = tvDisplayPanel.getSpecName();
        this.isPopularSpec = tvDisplayPanel.getIsPopularSpec();
    }
}
