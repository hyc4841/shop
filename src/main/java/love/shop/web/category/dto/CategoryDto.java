package love.shop.web.category.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.category.Category;

import java.util.List;

@Slf4j
@Data
public class CategoryDto {

    private Long categoryId;
    private String categoryName;
    private Long parent;
    private String parentName;
//    private List<ItemCategoryDto> itemCategories;
    private String type;
    private List<CategoryDto> children;
    private String subCategoryName;
    private Integer sequence;

    // 부모가 없을 수도 있다, 자식이 없을 수도 있다.

    public CategoryDto(Category category) {
        if (category != null) {
            this.categoryId = category.getId();
            this.categoryName = category.getCategoryName();
            this.parent = category.getParent() == null ? null : category.getParent().getId();
            this.parentName = category.getParent() == null ? null : category.getParent().getCategoryName();

            /*
            this.itemCategories = category.getItemCategories().stream()
                    .map(itemCategory -> new ItemCategoryDto(itemCategory))
                    .collect(Collectors.toList());
             */

            /*
            this.children = category.getChildren().stream()
                    .map(child -> new CategoryDto(child))
                    .collect(Collectors.toList());

             */

            this.type = category.getType();
            this.subCategoryName = category.getSubCategoryName();
            this.sequence = category.getSequence();

        }
    }

    public void addChildren(Category child) {
        children.add(new CategoryDto(child));
    }

    public CategoryDto() {
    }
}
