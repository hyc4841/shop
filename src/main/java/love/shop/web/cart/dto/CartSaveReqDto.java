package love.shop.web.cart.dto;

import lombok.Data;

@Data
public class CartSaveReqDto {

    private Long itemId;
    private Integer count;

}
