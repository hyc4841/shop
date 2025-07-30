package love.shop.web.itemSpec.dto.tv;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.spec.tv.TvSpec;
import love.shop.web.itemSpec.dto.ItemSpecDto;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TvSpecDto extends ItemSpecDto {

    private List<TvBrandDto> tvBrands;
    private List<TvDisplayPanelDto> tvDisplayPanels;
    private List<TvDisplayTypeDto> tvDisplayTypes;
    private List<TvHDRDto> tvHDRs;
    private List<TvManufacturerDto> tvManufacturers;
    private List<TvPictureQualityDto> tvPictureQualities;
    private List<TvProcessorDto> tvProcessors;
    private List<TvRefreshRateDto> tvRefreshRates;
    private List<TvResolutionDto> tvResolutions;
    private List<TvScreenSizeDto> tvScreenSizes;
    private List<TvSoundDto> tvSounds;
    private List<TvSpeakerChannelDto> tvSpeakerChannels;
    private List<TvSpeakerOutputDto> tvSpeakerOutputs;

    public TvSpecDto(TvSpec tvSpec) {
        this.tvBrands = tvSpec.getTvBrand().stream()
                .map(spec -> new TvBrandDto(spec))
                .collect(Collectors.toList());
        this.tvDisplayPanels = tvSpec.getTvDisplayPanels().stream()
                .map(spec -> new TvDisplayPanelDto(spec))
                .collect(Collectors.toList());
        this.tvDisplayTypes = tvSpec.getTvDisplayTypes().stream()
                .map(spec -> new TvDisplayTypeDto(spec))
                .collect(Collectors.toList());
        this.tvHDRs = tvSpec.getTvHDRS().stream()
                .map(spec -> new TvHDRDto(spec))
                .collect(Collectors.toList());
        this.tvManufacturers = tvSpec.getTvManufacturers().stream()
                .map(spec -> new TvManufacturerDto(spec))
                .collect(Collectors.toList());
        this.tvPictureQualities = tvSpec.getTvPictureQualities().stream()
                .map(spec -> new TvPictureQualityDto(spec))
                .collect(Collectors.toList());
        this.tvProcessors = tvSpec.getTvProcessors().stream()
                .map(spec -> new TvProcessorDto(spec))
                .collect(Collectors.toList());
        this.tvRefreshRates = tvSpec.getTvRefreshRates().stream()
                .map(spec -> new TvRefreshRateDto(spec))
                .collect(Collectors.toList());
        this.tvResolutions = tvSpec.getTvResolutions().stream()
                .map(spec -> new TvResolutionDto(spec))
                .collect(Collectors.toList());
        this.tvScreenSizes = tvSpec.getTvScreenSizes().stream()
                .map(spec -> new TvScreenSizeDto(spec))
                .collect(Collectors.toList());
        this.tvSounds = tvSpec.getTvSounds().stream()
                .map(spec -> new TvSoundDto(spec))
                .collect(Collectors.toList());
        this.tvSpeakerChannels = tvSpec.getTvSpeakerChannels().stream()
                .map(spec -> new TvSpeakerChannelDto(spec))
                .collect(Collectors.toList());
        this.tvSpeakerOutputs = tvSpec.getTvSpeakerOutputs().stream()
                .map(spec -> new TvSpeakerOutputDto(spec))
                .collect(Collectors.toList());
    }
}
