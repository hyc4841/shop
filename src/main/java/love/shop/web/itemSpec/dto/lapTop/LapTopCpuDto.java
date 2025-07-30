package love.shop.web.itemSpec.dto.lapTop;

import lombok.Data;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.lapTop.LapTopCpu;

@Data
@NoArgsConstructor
public class LapTopCpuDto {

    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    public LapTopCpuDto(LapTopCpu lapTopCpu) {
        this.id = lapTopCpu.getId();
        this.specName = lapTopCpu.getSpecName();
        this.isPopularSpec = lapTopCpu.getIsPopularSpec();
    }
}
