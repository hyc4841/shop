package love.shop.web.itemCart.dto;

import lombok.Data;
import love.shop.domain.itemCart.ItemCart;
import love.shop.web.item.dto.ItemDto;

@Data
public class ItemCartDto {

    private Long id;
    private ItemDto item;
    private Integer itemCount;

    public ItemCartDto(ItemCart itemCart) {
        this.id = itemCart.getId();
        this.item = itemCart.getItem() != null ? ItemDto.createItemDto(itemCart.getItem()) : null; // 걸리는 부분은 이부분
        this.itemCount = itemCart.getItemCount();
    }
}
