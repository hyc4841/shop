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

    private List<TvBrandDto> tvBrandList;
    private List<TvDisplayPanelDto> tvDisplayPanelList;
    private List<TvDisplayTypeDto> tvDisplayTypeList;
    private List<TvHDRDto> tvHDRList;
    private List<TvManufacturerDto> tvManufacturerList;
    private List<TvPictureQualityDto> tvPictureQualityList;
    private List<TvProcessorDto> tvProcessorList;
    private List<TvRefreshRateDto> tvRefreshRateList;
    private List<TvResolutionDto> tvResolutionList;
    private List<TvScreenSizeDto> tvScreenSizeList;
    private List<TvSoundDto> tvSoundList;
    private List<TvSpeakerChannelDto> tvSpeakerChannelList;
    private List<TvSpeakerOutputDto> tvSpeakerOutputList;

    public TvSpecDto(TvSpec tvSpec) {
        this.tvBrandList = tvSpec.getTvBrand().stream()
                .map(spec -> new TvBrandDto(spec))
                .collect(Collectors.toList());
        this.tvDisplayPanelList = tvSpec.getTvDisplayPanels().stream()
                .map(spec -> new TvDisplayPanelDto(spec))
                .collect(Collectors.toList());
        this.tvDisplayTypeList = tvSpec.getTvDisplayTypes().stream()
                .map(spec -> new TvDisplayTypeDto(spec))
                .collect(Collectors.toList());
        this.tvHDRList = tvSpec.getTvHDRS().stream()
                .map(spec -> new TvHDRDto(spec))
                .collect(Collectors.toList());
        this.tvManufacturerList = tvSpec.getTvManufacturers().stream()
                .map(spec -> new TvManufacturerDto(spec))
                .collect(Collectors.toList());
        this.tvPictureQualityList = tvSpec.getTvPictureQualities().stream()
                .map(spec -> new TvPictureQualityDto(spec))
                .collect(Collectors.toList());
        this.tvProcessorList = tvSpec.getTvProcessors().stream()
                .map(spec -> new TvProcessorDto(spec))
                .collect(Collectors.toList());
        this.tvRefreshRateList = tvSpec.getTvRefreshRates().stream()
                .map(spec -> new TvRefreshRateDto(spec))
                .collect(Collectors.toList());
        this.tvResolutionList = tvSpec.getTvResolutions().stream()
                .map(spec -> new TvResolutionDto(spec))
                .collect(Collectors.toList());
        this.tvScreenSizeList = tvSpec.getTvScreenSizes().stream()
                .map(spec -> new TvScreenSizeDto(spec))
                .collect(Collectors.toList());
        this.tvSoundList = tvSpec.getTvSounds().stream()
                .map(spec -> new TvSoundDto(spec))
                .collect(Collectors.toList());
        this.tvSpeakerChannelList = tvSpec.getTvSpeakerChannels().stream()
                .map(spec -> new TvSpeakerChannelDto(spec))
                .collect(Collectors.toList());
        this.tvSpeakerOutputList = tvSpec.getTvSpeakerOutputs().stream()
                .map(spec -> new TvSpeakerOutputDto(spec))
                .collect(Collectors.toList());
    }
}
