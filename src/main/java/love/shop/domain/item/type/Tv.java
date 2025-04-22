package love.shop.domain.item.type;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.item.Item;
import love.shop.web.item.saveDto.TVSaveReqDto;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tv extends Item {

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

    private final String dataType = "Tv";

    @Override
    public String getType() {
        return dataType;
    }

    public Tv(String name, int price, int stockQuantity, String tvBrands, String tvDisplayPanels, String tvDisplayTypes, String tvHDRs, String tvManufacturers, String tvPictureQualities, String tvProcessors, String tvRefreshRates, String tvResolutions, String tvScreenSizes, String tvSounds, String tvSpeakerChannels, String tvSpeakerOutputs) {
        super(name, price, stockQuantity);
        this.tvBrands = tvBrands;
        this.tvDisplayPanels = tvDisplayPanels;
        this.tvDisplayTypes = tvDisplayTypes;
        this.tvHDRs = tvHDRs;
        this.tvManufacturers = tvManufacturers;
        this.tvPictureQualities = tvPictureQualities;
        this.tvProcessors = tvProcessors;
        this.tvRefreshRates = tvRefreshRates;
        this.tvResolutions = tvResolutions;
        this.tvScreenSizes = tvScreenSizes;
        this.tvSounds = tvSounds;
        this.tvSpeakerChannels = tvSpeakerChannels;
        this.tvSpeakerOutputs = tvSpeakerOutputs;
    }

    public Tv(TVSaveReqDto tvDto) {
        super(tvDto.getName(), tvDto.getPrice(), tvDto.getStockQuantity());
        this.tvBrands = tvDto.getTvBrands();
        this.tvDisplayPanels = tvDto.getTvDisplayPanels();
        this.tvDisplayTypes = tvDto.getTvDisplayTypes();
        this.tvHDRs = tvDto.getTvHDRs();
        this.tvManufacturers = tvDto.getTvManufacturers();
        this.tvPictureQualities = tvDto.getTvPictureQualities();
        this.tvProcessors = tvDto.getTvProcessors();
        this.tvRefreshRates = tvDto.getTvRefreshRates();
        this.tvResolutions = tvDto.getTvResolutions();
        this.tvScreenSizes = tvDto.getTvScreenSizes();
        this.tvSounds = tvDto.getTvSounds();
        this.tvSpeakerChannels = tvDto.getTvSpeakerChannels();
        this.tvSpeakerOutputs = tvDto.getTvSpeakerOutputs();
    }
}
