package love.shop.domain.itemSpec;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import love.shop.web.item.filter.tv.*;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TVSpec {

    private List<TVBrand> tvBrands;
    private List<TVDisplayPanel> tvDisplayPanels;
    private List<TVDisplayType> tvDisplayTypes;
    private List<TVHDR> tvHDRs;
    private List<TVManufacturer> tvManufacturers;
    private List<String> tvPictureQualities;
    private List<TVProcessor> tvProcessors;
    private List<TVRefreshRate> tvRefreshRates;
    private List<TVResolution> tvResolutions;
    private List<String> tvScreenSizes;
    private List<String> tvSounds;
    private List<String> tvSpeakerChannels;
    private List<String> tvSpeakerOutputs;

}
