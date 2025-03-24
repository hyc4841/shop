package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.TV;
import love.shop.web.item.filter.tv.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class TVDto extends ItemDto{

    private TVBrand brand;
    private TVDisplayPanel displayPanel;
    private TVDisplayType displayType;
    private TVHDR hdr;
    private TVManufacturer manufacturer;
    private TVPictureQuality pictureQuality;
    private TVProcessor processor;
    private TVRefreshRate refreshRate;
    private TVScreenSize screenSize;
    private String sound;
    private TVSpeakerChannel speakerChannel;
    private TVSpeakerOutput speakerOutput;

    public TVDto(TV tv) {
        super(tv.getName(), tv.getPrice(), tv.getStockQuantity());
        this.brand = tv.getBrand();
        this.displayPanel = tv.getDisplayPanel();
        this.displayType = tv.getDisplayType();
        this.hdr = tv.getHdr();
        this.manufacturer = tv.getManufacturer();
        this.pictureQuality = tv.getPictureQuality();
        this.processor = tv.getProcessor();
        this.refreshRate = tv.getRefreshRate();
        this.screenSize = tv.getScreenSize();
        this.sound = tv.getSound();
        this.speakerChannel = tv.getSpeakerChannel();
        this.speakerOutput = tv.getSpeakerOutput();
    }
}
