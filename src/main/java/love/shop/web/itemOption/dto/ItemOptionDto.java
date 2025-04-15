package love.shop.web.itemOption.dto;

import lombok.Data;
import love.shop.domain.item.Item;
import love.shop.domain.itemOption.ItemOption;
import love.shop.web.item.dto.ItemDto;

import java.util.List;

@Data
public class ItemOptionDto {

    private Long id;
    private String optionName;
    private Long parentId;
//    private List<ItemOptionDto> child;
    private ItemDto item;
    private Long salesPageId;


    public ItemOptionDto(ItemOption itemOption) {
        this.id = itemOption.getId();
        this.optionName = itemOption.getOptionName();
        if (itemOption.getParent() != null) {
            this.parentId = itemOption.getParent().getId();
        } else {
            this.parentId = null;
        }
//        this.child = itemOption.get;
        if (itemOption.getItem() != null) {
            this.item = ItemDto.createItemDto(itemOption.getItem());
        } else {
            this.item = null;
        }
        this.salesPageId = itemOption.getSalesPage().getId();
    }


}
