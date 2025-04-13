package love.shop.web.item.searchFilter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.web.item.filter.tv.*;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TVSearchFilter extends SearchFilter {

    private List<TVBrand> tvBrands;
    private List<TVDisplayPanel> tvDisplayPanels;
    private List<TVDisplayType> tvDisplayTypes;
    private List<TVHDR> tvHDRs;
    private List<TVManufacturer> tvManufacturers;
    private List<TVPictureQuality> tvPictureQualities;
    private List<TVProcessor> tvProcessors;
    private List<TVRefreshRate> tvRefreshRates;
    private List<TVResolution> tvResolutions;
    private List<TVScreenSize> tvScreenSizes;
    private List<String> tvSounds;
    private List<TVSpeakerChannel> tvSpeakerChannels;
    private List<TVSpeakerOutput> tvSpeakerOutputs;

    public static TVSearchFilter createTVSearchFilter() {
        TVSearchFilter tvSearchFilter = new TVSearchFilter();
        tvSearchFilter.setTvBrands(Arrays.asList(TVBrand.values()));
        tvSearchFilter.setTvDisplayPanels(Arrays.asList(TVDisplayPanel.values()));
        tvSearchFilter.setTvDisplayTypes(Arrays.asList(TVDisplayType.values()));
        tvSearchFilter.setTvHDRs(Arrays.asList(TVHDR.values()));
        tvSearchFilter.setTvManufacturers(Arrays.asList(TVManufacturer.values()));
        tvSearchFilter.setTvPictureQualities(Arrays.asList(TVPictureQuality.values()));
        tvSearchFilter.setTvProcessors(Arrays.asList(TVProcessor.values()));
        tvSearchFilter.setTvRefreshRates(Arrays.asList(TVRefreshRate.values()));
        tvSearchFilter.setTvResolutions(Arrays.asList(TVResolution.values()));
        tvSearchFilter.setTvScreenSizes(Arrays.asList(TVScreenSize.values()));
        tvSearchFilter.setTvSounds(TVSound.createTVSoundList()); // 이렇게 그냥 string 으로 해도 괜찮을거 같은데?
        tvSearchFilter.setTvSpeakerChannels(Arrays.asList(TVSpeakerChannel.values()));
        tvSearchFilter.setTvSpeakerOutputs(Arrays.asList(TVSpeakerOutput.values()));

        return tvSearchFilter;
    }

}
