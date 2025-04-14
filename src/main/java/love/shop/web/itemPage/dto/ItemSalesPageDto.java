package love.shop.web.itemPage.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.item.Item;
import love.shop.domain.itemSalesPage.ItemSalesPage;
import love.shop.web.item.dto.ItemDto;

@Slf4j
@Data
public class ItemSalesPageDto {

    private Long id;

    private ItemDto item; // 이거 추후에 itemDto로 바꿔야함.

    private Long pageId;

    private String optionName;

    private Boolean isMainItem;

    private String itemDisplayName;

    private Integer itemPrice;
    private Integer itemQuantity;


    public ItemSalesPageDto(ItemSalesPage itemPage) {
        this.id = itemPage.getId();
        this.item = ItemDto.createItemDto(Item.proxyToEntity(itemPage.getItem()));
        this.pageId = itemPage.getSalesPage().getId();
        this.optionName = itemPage.getOptionName();
        this.isMainItem = itemPage.getIsMainItem();
        this.itemDisplayName = itemPage.getItemDisplayName();
        this.itemPrice = itemPage.getItem().getPrice();
        this.itemQuantity = itemPage.getItem().getStockQuantity();
    }
}
