package love.shop.web.item.searchCond;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LapTopSearchCond.class, name = "LapTop"),
        @JsonSubTypes.Type(value = BookSearchCond.class, name = "Book"),
        @JsonSubTypes.Type(value = BeamScreenSearchCond.class, name = "BeamScreen"),
        @JsonSubTypes.Type(value = DeskTopSearchCond.class, name = "DeskTop"),
        @JsonSubTypes.Type(value = InkCartridgeSearchCond.class, name = "InkCartridge"),
        @JsonSubTypes.Type(value = MFPSearchCond.class, name = "MFP"),
        @JsonSubTypes.Type(value = MonitorSearchCond.class, name = "Monitor"),
        @JsonSubTypes.Type(value = PrinterSearchCond.class, name = "Printer"),
        @JsonSubTypes.Type(value = ProjectorSearchCond.class, name = "Projector"),
        @JsonSubTypes.Type(value = ScannerSearchCond.class, name = "Scanner"),
        @JsonSubTypes.Type(value = SmartPhoneSearchCond.class, name = "SmartPhone"),
        @JsonSubTypes.Type(value = StreamingDongleSearchCond.class, name = "StreamingDongle"),
        @JsonSubTypes.Type(value = StreamingMediaPlayerSearchCond.class, name = "streamingMediaPlayer"),
        @JsonSubTypes.Type(value = TonerCartridgeSearchCond.class, name = "TonerCartridge"),
        @JsonSubTypes.Type(value = WiredEarbudsSearchCond.class, name = "WiredEarbuds"),
        @JsonSubTypes.Type(value = WiredHeadphonesSearchCond.class, name = "WiredHeadphones"),
        @JsonSubTypes.Type(value = WiredHeadsetSearchCond.class, name = "WiredHeadset"),
        @JsonSubTypes.Type(value = WirelessEarbudsSearchCond.class, name = "WirelessEarbuds"),
        @JsonSubTypes.Type(value = WirelessHeadphonesSearchCond.class, name = "WirelessHeadphones"),
        @JsonSubTypes.Type(value = WirelessHeadsetSearchCond.class, name = "WirelessHeadset"),
})
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCond {

    private String type; // 자식 클래스 이름

    // 검색 조건
    private Long categories;
    private String itemName;

    // 가격 범위
    private Integer morePrice; // 이상
    private Integer lessPrice; // 이하가격
    // morePrice 는 lessPrice 보다 클 수 없다

}
