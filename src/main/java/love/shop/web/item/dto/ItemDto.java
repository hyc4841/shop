package love.shop.web.item.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.item.type.*;
import love.shop.domain.item.Item;
import love.shop.web.itemCategory.dto.ItemCategoryDto;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@Slf4j
public abstract class ItemDto {

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
                case "TV":
                    itemDtoList.add(new TVDto((Tv) item));
                    break;

                default:
                    log.info("유효한 카테고리가 없는 상품={}", item);
            }
        }

        return itemDtoList;
    }


    public static ItemDto createItemDto(Item item) {
        log.info("타입 확인={}", item.getType());

        return switch (item.getType()) {
            case "LapTop" -> new LapTopDto((LapTop) item);
            case "Book" -> new BookDto((Book) item);
            case "SmartPhone" -> new SmartPhoneDto((SmartPhone) item);
            case "Projector" -> new ProjectorDto((Projector) item);
            case "BeamScreen" -> new BeamScreenDto((BeamScreen) item);
            case "StreamingDongle" -> new StreamingDongleDto((StreamingDongle) item);
            case "streamingMediaPlayer" -> new StreamingMediaPlayerDto((streamingMediaPlayer) item);
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
            case "TV" -> new TVDto((Tv) item);

            default -> {
                log.info("유효한 카테고리가 없는 상품={}", item);
                yield null;
            }
        };
    }



}
