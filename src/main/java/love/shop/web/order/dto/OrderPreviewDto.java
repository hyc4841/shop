package love.shop.web.order.dto;

import lombok.Data;
import love.shop.web.item.dto.ItemDto;

@Data
public class OrderPreviewDto {

    private ItemDto item;
    private Integer quantity;

    public OrderPreviewDto(ItemDto item, Integer quantity) {
        this.item = item;
        this.quantity = quantity;
    }
}
