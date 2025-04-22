package love.shop.web.item.searchFilter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.web.item.spec.tv.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TVSearchFilter extends SearchFilter {

    private List<TvBrand> tvBrands;
    private List<TvDisplayPanel> tvDisplayPanels;
    private List<TvDisplayType> tvDisplayTypes;
    private List<TvHDR> tvHDRS;
    private List<TvManufacturer> tvManufacturers;
    private List<String> tvPictureQualities;
    private List<TvProcessor> tvProcessors;
    private List<TvRefreshRate> tvRefreshRates;
    private List<TvResolution> tvResolutions;
    private List<String> tvScreenSizes;
    private List<String> tvSounds;
    private List<String> tvSpeakerChannels;
    private List<String> tvSpeakerOutputs;

    public static TVSearchFilter createTVSearchFilter() {
        TVSearchFilter tvSearchFilter = new TVSearchFilter();
        /*
        tvSearchFilter.setTvBrands(Arrays.asList(TVBrand.values()));
        tvSearchFilter.setTvDisplayPanels(Arrays.asList(TVDisplayPanel.values()));
        tvSearchFilter.setTvDisplayTypes(Arrays.asList(TVDisplayType.values()));
        tvSearchFilter.setTvHDRs(Arrays.asList(TVHDR.values()));
        tvSearchFilter.setTvManufacturers(Arrays.asList(TVManufacturer.values()));
        tvSearchFilter.setTvPictureQualities(TVPictureQuality.createTVPictureQualityList());
        tvSearchFilter.setTvProcessors(Arrays.asList(TVProcessor.values()));
        tvSearchFilter.setTvRefreshRates(Arrays.asList(TVRefreshRate.values()));
        tvSearchFilter.setTvResolutions(Arrays.asList(TVResolution.values()));
        tvSearchFilter.setTvScreenSizes(TVScreenSize.createTVScreenSizeList());
        tvSearchFilter.setTvSounds(TVSound.createTVSoundList()); // 이렇게 그냥 string 으로 해도 괜찮을거 같은데?
        tvSearchFilter.setTvSpeakerChannels(TVSpeakerChannel.createTVSpeakerChannelList());
        tvSearchFilter.setTvSpeakerOutputs(TVSpeakerOutput.createTVSpeakerOutputlList());
         */

        return tvSearchFilter;
    }

}
