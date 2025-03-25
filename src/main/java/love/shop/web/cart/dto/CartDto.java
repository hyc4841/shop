package love.shop.web.cart.dto;

import lombok.Data;
import love.shop.domain.cart.Cart;
import love.shop.web.itemCart.dto.ItemCartDto;
import love.shop.web.login.dto.MemberDto;

import java.util.List;

@Data
public class CartDto {

    private Long id;
    private List<ItemCartDto> itemCarts;
    private MemberDto member;

    public CartDto(Cart cart) {
        this.id = cart.getId();
        this.itemCarts = cart.getItemCartList().stream().
                map(itemCart -> new ItemCartDto(itemCart)).toList();
        this.member = member;
    }
}
