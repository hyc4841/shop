package love.shop.web.itemPage.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.itemPage.ItemPage;

@Slf4j
@Data
public class ItemPageDto {

    private Long id;

    private Long itemId;

    private Long pageId;

    private String optionName;


    public ItemPageDto(ItemPage itemPage) {
        this.id = itemPage.getId();
        this.itemId = itemPage.getItem().getId();
        this.pageId = itemPage.getPage().getId();
        this.optionName = itemPage.getOptionName();
    }
}
