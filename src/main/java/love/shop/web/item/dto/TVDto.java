package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.TV;
import love.shop.web.item.filter.tv.*;
import love.shop.web.itemCategory.dto.ItemCategoryDto;

import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class TVDto extends ItemDto{

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

    public TVDto(TV tv) {
        super(tv.getName(), tv.getPrice(), tv.getStockQuantity());
        super.setItemCategories(tv.getItemCategories().stream()
                .map(itemCategory -> new ItemCategoryDto(itemCategory))
                .collect(Collectors.toList()));

        this.tvBrands = tv.getTvBrands();
        this.tvDisplayPanels = tv.getTvDisplayPanels();
        this.tvDisplayTypes = tv.getTvDisplayTypes();
        this.tvHDRs = tv.getTvHDRs();
        this.tvManufacturers = tv.getTvManufacturers();
        this.tvPictureQualities = tv.getTvPictureQualities();
        this.tvProcessors = tv.getTvProcessors();
        this.tvRefreshRates = tv.getTvRefreshRates();
        this.tvResolutions = tv.getTvResolutions();
        this.tvScreenSizes = tv.getTvScreenSizes();
        this.tvSounds = tv.getTvSounds();
        this.tvSpeakerChannels = tv.getTvSpeakerChannels();
        this.tvSpeakerOutputs = tv.getTvSpeakerOutputs();
    }
}
