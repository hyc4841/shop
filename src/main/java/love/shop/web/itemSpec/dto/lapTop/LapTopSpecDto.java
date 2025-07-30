package love.shop.web.itemSpec.dto.lapTop;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.lapTop.LapTopBrand;
import love.shop.domain.itemSpec.spec.lapTop.LapTopSpec;
import love.shop.web.itemSpec.dto.ItemSpecDto;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LapTopSpecDto extends ItemSpecDto {

    private List<LapTopBrandDto> lapTopBrand;
    private List<LapTopCpuDto> lapTopCpu;
    private List<LapTopManufactureBrandDto> lapTopStorage;
    private List<LapTopScreenSizeDto> lapTopScreenSize;
    private List<LapTopStorageDto> lapTopManufactureBrand;

    public LapTopSpecDto(LapTopSpec lapTopSpec) {
        this.lapTopBrand = lapTopSpec.getLapTopBrands().stream()
                .map(spec -> new LapTopBrandDto(spec))
                .collect(Collectors.toList());
        this.lapTopCpu = lapTopSpec.getLapTopCpus().stream()
                .map(spec -> new LapTopCpuDto(spec))
                .collect(Collectors.toList());
        this.lapTopStorage = lapTopSpec.getLapTopManufactureBrands().stream()
                .map(spec -> new LapTopManufactureBrandDto(spec))
                .collect(Collectors.toList());
        this.lapTopScreenSize = lapTopSpec.getLapTopScreenSizes().stream()
                .map(spec -> new LapTopScreenSizeDto(spec))
                .collect(Collectors.toList());
        this.lapTopManufactureBrand = lapTopSpec.getLapTopStorages().stream()
                .map(spec -> new LapTopStorageDto(spec))
                .collect(Collectors.toList());
    }
}
