package love.shop.web.cart.dto;

import lombok.Data;

@Data
public class AddItemToCartDto {

    private Long itemId;
    private Integer count;

}
