package love.shop.web.item.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public abstract class ItemDto {

    private String name;
    private int price;
    private int stockQuantity;

    private List<ItemCategoryDto> itemCategories;
}
