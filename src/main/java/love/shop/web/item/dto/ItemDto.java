package love.shop.web.item.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import love.shop.domain.item.Item;
import love.shop.domain.item.LapTop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.CallSite;
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

    public static List<ItemDto> createItemDto(List<Item> itemList) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            switch (item.getType()) {
                case "LapTop":
                    itemDtoList.add(new LapTopDto((LapTop) item));
                    break;


                default:
                    log.info("유효한 카테고리가 없는 상품={}", item);
            }
        }

        return itemDtoList;
    }




}
