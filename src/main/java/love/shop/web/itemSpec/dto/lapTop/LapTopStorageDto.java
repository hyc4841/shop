package love.shop.web.itemSpec.dto.lapTop;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.lapTop.LapTopStorage;

@Data
@NoArgsConstructor
public class LapTopStorageDto {

    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    public LapTopStorageDto(LapTopStorage lapTopStorage) {
        this.id = lapTopStorage.getId();
        this.specName = lapTopStorage.getSpecName();
        this.isPopularSpec = lapTopStorage.getIsPopularSpec();
    }
}
