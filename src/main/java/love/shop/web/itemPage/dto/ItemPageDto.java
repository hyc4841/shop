package love.shop.web.itemPage.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.itemSalesPage.ItemSalesPage;

@Slf4j
@Data
public class ItemPageDto {

    private Long id;

    private Long itemId;

    private Long pageId;

    private String optionName;

    private Boolean isMainItem;

    private String itemDisplayName;

    private Integer itemPrice;
    private Integer itemQuantity;


    public ItemPageDto(ItemSalesPage itemPage) {
        this.id = itemPage.getId();
        this.itemId = itemPage.getItem().getId();
        this.pageId = itemPage.getSalesPage().getId();
        this.optionName = itemPage.getOptionName();
        this.isMainItem = itemPage.getIsMainItem();
        this.itemDisplayName = itemPage.getItemDisplayName();
        this.itemPrice = itemPage.getItem().getPrice();
        this.itemQuantity = itemPage.getItem().getStockQuantity();
    }
}
