package love.shop.web.item.saveDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.web.item.filter.tv.*;

import java.util.List;

@JsonTypeName(value = "Tv")
@EqualsAndHashCode(callSuper = true)
@Data
public class TVSaveReqDto extends ItemSaveReqDto {

    private TVBrand brand;
    private TVDisplayPanel displayPanel;
    private TVDisplayType displayType;
    private TVHDR hdr;
    private TVManufacturer manufacturer;
    private TVPictureQuality pictureQuality;
    private TVProcessor processor;
    private TVRefreshRate refreshRate;
    private TVScreenSize screenSize;
    private TVSound sound;
    private TVSpeakerChannel speakerChannel;
    private TVSpeakerOutput speakerOutput;

    public TVSaveReqDto(String name, int price, int stockQuantity, List<Integer> categoriesId, String type, TVBrand brand, TVDisplayPanel displayPanel, TVDisplayType displayType, TVHDR hdr, TVManufacturer manufacturer, TVPictureQuality pictureQuality, TVProcessor processor, TVRefreshRate refreshRate, TVScreenSize screenSize, TVSound sound, TVSpeakerChannel speakerChannel, TVSpeakerOutput speakerOutput) {
        super(name, price, stockQuantity, categoriesId, type);
        this.brand = brand;
        this.displayPanel = displayPanel;
        this.displayType = displayType;
        this.hdr = hdr;
        this.manufacturer = manufacturer;
        this.pictureQuality = pictureQuality;
        this.processor = processor;
        this.refreshRate = refreshRate;
        this.screenSize = screenSize;
        this.sound = sound;
        this.speakerChannel = speakerChannel;
        this.speakerOutput = speakerOutput;
    }

}
