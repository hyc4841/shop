package love.shop.domain.item.type;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.item.Item;
import love.shop.web.item.filter.tv.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TV extends Item {

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

    private final String dataType = "Tv";

    @Override
    public String getType() {
        return dataType;
    }
}
