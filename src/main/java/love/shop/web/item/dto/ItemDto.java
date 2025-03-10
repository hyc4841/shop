package love.shop.web.item.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import love.shop.domain.item.type.*;
import love.shop.domain.item.Item;
import love.shop.web.item.saveDto.BookSaveReqDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public abstract class ItemDto {

    private static final Logger log = LoggerFactory.getLogger(ItemDto.class);
    private String name;
    private int price;
    private int stockQuantity;

    private List<ItemCategoryDto> itemCategories;

    public ItemDto(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public ItemDto() {
    }



    // 리스트로 들어온 아이템을 각자 알맞은 Dto로 만들어주는 메서드
    public static List<ItemDto> createItemDtoList(List<Item> itemList) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            switch (item.getType()) {
                case "LapTop":
                    itemDtoList.add(new LapTopDto((LapTop) item));
                    break;
                case "Book":
                    itemDtoList.add(new BookDto((Book) item));
                    break;


                default:
                    log.info("유효한 카테고리가 없는 상품={}", item);
            }
        }

        return itemDtoList;
    }


    public static ItemDto createItemDto(Item item) {

        return switch (item.getType()) {
            case "LapTop" -> new LapTopDto((LapTop) item);
            case "Book" -> new BookDto((Book) item);
            case "SmartPhone" -> new SmartPhoneDto((SmartPhone) item);
            case "Projector" -> new ProjectorDto((Projector) item);
            case "BeamScreen" -> new BeamScreenDto((BeamScreen) item);
            case "StreamingDongle" -> new StreamingDongleDto((StreamingDongle) item);
            case "streamingMediaPlayer" -> new streamingMediaPlayerDto((streamingMediaPlayer) item);
            case "DeskTop" -> new DeskTopDto((DeskTop) item);
            case "Monitor" -> new MonitorDto((Monitor) item);
            case "MFP" -> new MFPDto((MFP) item);
            case "Printer" -> new PrinterDto((Printer) item);
            case "TonerCartridge" -> new TonerCartridgeDto((TonerCartridge) item);
            case "InkCartridge" -> new InkCartridgeDto((InkCartridge) item);
            case "Scanner" -> new ScannerDto((Scanner) item);
            case "WirelessEarbuds" -> new WirelessEarbudsDto((WirelessEarbuds) item);
            case "WirelessHeadphones" -> new WirelessHeadphonesDto((WirelessHeadphones) item);
            case "WiredEarbuds" -> new WiredEarbudsDto((WiredEarbuds) item);
            case "WiredHeadphones" -> new WiredHeadphonesDto((WiredHeadphones) item);
            case "WirelessHeadset" -> new WirelessHeadsetDto((WirelessHeadset) item);
            case "WiredHeadset" -> new WiredHeadsetDto((WiredHeadset) item);

            default -> {
                log.info("유효한 카테고리가 없는 상품={}", item);
                yield null;
            }
        };

    }



}
