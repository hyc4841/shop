package love.shop.web.item.saveDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@JsonTypeName(value = "TV")
@EqualsAndHashCode(callSuper = true)
@Data
public class TVSaveReqDto extends ItemSaveReqDto {

    private String tvBrands;
    private String tvDisplayPanels;
    private String tvDisplayTypes;
    private String tvHDRs;
    private String tvManufacturers;
    private String tvPictureQualities;
    private String tvProcessors;
    private String tvRefreshRates;
    private String tvResolutions;
    private String tvScreenSizes;
    private String tvSounds;
    private String tvSpeakerChannels;
    private String tvSpeakerOutputs;

    public TVSaveReqDto() {

    }

    public TVSaveReqDto(String name, int price, int stockQuantity, List<Integer> categoriesId, String dataType,
                        String tvBrands, String tvDisplayPanels, String tvDisplayTypes, String tvHDRs,
                        String tvManufacturers, String tvPictureQualities, String tvProcessors,
                        String tvRefreshRates, String tvResolutions, String tvScreenSizes,
                        String tvSounds, String tvSpeakerChannels, String tvSpeakerOutputs) {

        super(name, price, stockQuantity, categoriesId, dataType);
        this.tvBrands = tvBrands;
        this.tvDisplayPanels = tvDisplayPanels;
        this.tvDisplayTypes = tvDisplayTypes;
        this.tvHDRs = tvHDRs;
        this.tvManufacturers = tvManufacturers;
        this.tvPictureQualities = tvPictureQualities; // 해상도
        this.tvProcessors = tvProcessors;
        this.tvRefreshRates = tvRefreshRates;
        this.tvResolutions = tvResolutions;
        this.tvScreenSizes = tvScreenSizes;
        this.tvSounds = tvSounds;
        this.tvSpeakerChannels = tvSpeakerChannels;
        this.tvSpeakerOutputs = tvSpeakerOutputs;
    }
}
