package love.shop.web.item.searchFilter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.itemSpec.ItemSpec;
import love.shop.domain.itemSpec.TvSpec;
import love.shop.web.itemSpec.dto.ItemSpecDto;
import love.shop.web.itemSpec.dto.tv.TvSpecDto;

@Slf4j
@Data
public class SearchFilter {

    // 필터링 조건들을 데이터베이스에 넣어놓고 사용할건지 아님 이렇게 하드코딩으로 할건지
    // 근데 제대로 할려면 db에 넣는게 맞음. db에 넣으면 관리도 쉽게 가능함.

    public static ItemSpecDto findFilter(String type, ItemSpec itemSpec) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case "LapTop":
//                return LapTopSearchFilter.createLapTopFilter();
            case "Book":
                break;
            case "Tv":
                return new TvSpecDto((TvSpec) itemSpec);
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
