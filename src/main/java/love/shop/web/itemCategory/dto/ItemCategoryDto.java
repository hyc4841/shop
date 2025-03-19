package love.shop.web.itemCategory.dto;

import lombok.Data;
import love.shop.domain.ItemCategory.ItemCategory;

@Data
public class ItemCategoryDto {
    private String categoryName;
    private String itemName;

    /*
    public ItemCategoryDto(ItemCategory itemCategory) {
        this.categoryName = itemCategory.getCategory().getCategoryName();
    }
     */

    // 카테고리 입장에서 해당 카테고리에 어떤 아이템이 있는지 볼 수 있음
    public ItemCategoryDto(ItemCategory itemCategory) {
        this.itemName = itemCategory.getItem().getName();
        this.categoryName = itemCategory.getCategory().getCategoryName();
    }
}
