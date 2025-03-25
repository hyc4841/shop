package love.shop.domain.itemCart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.cart.Cart;
import love.shop.domain.item.Item;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCart {

    @Id
    @GeneratedValue
    @Column(name = "item_cart_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer itemCount; // 장바구니에 있는 해당 아이템 수량


    public static ItemCart createItemCart(Item item, Integer itemCount) {
        ItemCart itemCart = new ItemCart(item, itemCount);
        item
    }

    public ItemCart(Item item, Integer itemCount) {
        this.item = item;
        this.itemCount = itemCount;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
