package love.shop.web.item.dto;

import lombok.Data;
import love.shop.domain.ItemCategory.ItemCategory;

@Data
public class ItemCategoryDto {
    private String categoryName;

    public ItemCategoryDto(ItemCategory itemCategory) {
        this.categoryName = itemCategory.getCategory().getCategoryName();
    }
}
