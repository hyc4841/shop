package love.shop.web.item.saveDto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

// type 라는 이름의 필드로 어떤 클래스로 json을 받을지 선택하는 것. 이때 type 필드는 클래스 내 필드 명으로 사용할 수 없음. 이거 트러블 슈팅으로 쓰자
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TVSaveReqDto.class, name = "TV"),
        @JsonSubTypes.Type(value = LapTopSaveReqDto.class, name = "LapTop"),
        @JsonSubTypes.Type(value = BookSaveReqDto.class, name = "Book"),
        @JsonSubTypes.Type(value = BeamScreenSaveReqDto.class, name = "BeamScreen"),
        @JsonSubTypes.Type(value = DeskTopSaveReqDto.class, name = "DeskTop"),
        @JsonSubTypes.Type(value = InkCartridgeSaveReqDto.class, name = "InkCartridge"),
        @JsonSubTypes.Type(value = MFPSaveReqDto.class, name = "MFP"),
        @JsonSubTypes.Type(value = MonitorSaveReqDto.class, name = "Monitor"),
        @JsonSubTypes.Type(value = PrinterSaveReqDto.class, name = "Printer"),
        @JsonSubTypes.Type(value = ProjectorSaveReqDto.class, name = "Projector"),
        @JsonSubTypes.Type(value = ScannerSaveReqDto.class, name = "Scanner"),
        @JsonSubTypes.Type(value = SmartPhoneSaveReqDto.class, name = "SmartPhone"),
        @JsonSubTypes.Type(value = StreamingDongleSaveReqDto.class, name = "StreamingDongle"),
        @JsonSubTypes.Type(value = StreamingMediaPlayerSaveReqDto.class, name = "streamingMediaPlayer"),
        @JsonSubTypes.Type(value = TonerCartridgeSaveReqDto.class, name = "TonerCartridge"),
        @JsonSubTypes.Type(value = WiredEarbudsSaveReqDto.class, name = "WiredEarbuds"),
        @JsonSubTypes.Type(value = WiredHeadphonesSaveReqDto.class, name = "WiredHeadphones"),
        @JsonSubTypes.Type(value = WiredHeadsetSaveReqDto.class, name = "WiredHeadset"),
        @JsonSubTypes.Type(value = WirelessEarbudsSaveReqDto.class, name = "WirelessEarbuds"),
        @JsonSubTypes.Type(value = WirelessHeadphonesSaveReqDto.class, name = "WirelessHeadphones"),
        @JsonSubTypes.Type(value = WirelessHeadsetSaveReqDto.class, name = "WirelessHeadset"),
})
@Data
@RequiredArgsConstructor
public class ItemSaveReqDto {

    private String dataType;
    @NotBlank
    private String name;
    @NotNull
    private Integer price;
    @NotNull
    private Integer stockQuantity;

    private List<Integer> categoriesId; // 카테고리도 반드시 있어야하긴 한데..


    public ItemSaveReqDto(String name, int price, int stockQuantity, List<Integer> categoriesId, String dataType) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoriesId = categoriesId;
        this.dataType = dataType;
    }

    public String getType() {
        return dataType;
    }


}


/*
// ItemSaveReqDto API

{
---- 공통 필드 ----
"type": "LapTop",
"name": "testLapTop",
"price": 50000,
"stockQuantity": 9999,

---- 각 객체에 맞는 필드 ----
"categoriesId": [3, 32],
"dataType": "LapTop",
"lapTopBrand": "그램15",
"lapTopCpu": "코어i9",
"lapTopStorage": "TB_1",
"lapTopScreenSize": "Inch_15",
"lapTopManufactureBrand": "LG전자"}

 */

