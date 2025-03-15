package love.shop.web.category.dto;

import lombok.Data;
import love.shop.domain.category.Category;
import love.shop.web.itemCategory.dto.ItemCategoryDto;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CategoryDto {

    private Long categoryId;
    private String categoryName;
    private Long parent;
    private String parentName;
    private List<ItemCategoryDto> itemCategories;
    private List<CategoryDto> children;


    // 부모가 없을 수도 있다, 자식이 없을 수도 있다.
    //

    public CategoryDto(Category category) {
        if (category != null) {
            this.categoryId = category.getId();
            this.categoryName = category.getCategoryName();
            this.parent = category.getParent() == null ? null : category.getParent().getId();
            this.parentName = category.getParent() == null ? null : category.getParent().getCategoryName();
            this.itemCategories = category.getItemCategories().stream()
                    .map(itemCategory -> new ItemCategoryDto(itemCategory))
                    .collect(Collectors.toList());
            this.children = category.getChildren().stream()
                    .map(child -> new CategoryDto(child))
                    .collect(Collectors.toList());

        }
    }
}
