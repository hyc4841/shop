package love.shop.web.item.searchFilter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class SearchFilter {


    public static SearchFilter findFilter(String type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case "LapTop":
                return LapTopSearchFilter.createLapTopFilter();
            case "Book":
                break;
            case "TV":
                return TVSearchFilter.createTVSearchFilter();
            case "SmartPhone":
                break;
            case "Projector":
                break;
            case "BeamScreen":
                break;
            case "StreamingDongle":
                break;
            case "streamingMediaPlayer":
                break;
            case "DeskTop":
                break;
            case "Monitor":
                break;
            case "MFP":
                break;
            case "Printer":
                break;
            case "TonerCartridge":
                break;
            case "InkCartridge":
                break;
            case "Scanner":
                break;
            case "WirelessEarbuds":
                break;
            case "WirelessHeadphones":
                break;
            case "WiredEarbuds":
                break;
            case "WiredHeadphones":
                break;
            case "WirelessHeadset":
                break;
            case "WiredHeadset":
                break;
            default:
                log.info("맞는 타입 없음");
                return null;
        }
        return null;
    }


}
