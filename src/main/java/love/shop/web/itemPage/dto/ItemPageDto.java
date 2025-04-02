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


    public ItemPageDto(ItemSalesPage itemPage) {
        this.id = itemPage.getId();
        this.itemId = itemPage.getItem().getId();
        this.pageId = itemPage.getSalesPage().getId();
        this.optionName = itemPage.getOptionName();
    }
}
