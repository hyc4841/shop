package love.shop.web.cart.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.cart.Cart;
import love.shop.web.itemCart.dto.ItemCartDto;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Slf4j
public class CartDto {

    private Long id;
    private Long member;
    private List<ItemCartDto> itemCarts;

    /*
    public CartDto(Cart cart) {
        this.id = cart.getId();
        this.member = cart.getMember().getId();
        this.itemCarts = cart.getItemCarts() != null ? cart.getItemCarts().stream()
                .map(itemCart -> new ItemCartDto(itemCart))
                .collect(Collectors.toList()) : null;
    }

     */
}
