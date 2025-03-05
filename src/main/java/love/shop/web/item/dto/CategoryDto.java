package love.shop.web.item.dto;

import lombok.Data;
import love.shop.domain.category.Category;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CategoryDto {

    private Long categoryId;
    private CategoryDto parent;
    private List<CategoryDto> children;
    private String categoryName;
    private List<ItemCategoryDto> itemCategories;

    public CategoryDto(Category category) {
        this.categoryId = category.getId();
        this.parent = new CategoryDto(category.getParent());
        this.children = category.getChildren().stream()
                .map(child -> new CategoryDto(child))
                .collect(Collectors.toList());
        this.categoryName = category.getCategoryName();
        this.itemCategories = category.getItemCategories().stream()
                .map(itemCategory -> new ItemCategoryDto(itemCategory))
                .collect(Collectors.toList());
    }
}
