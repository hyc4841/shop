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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "item_count")
    private Integer itemCount; // 장바구니에 있는 해당 아이템 수량

    public static ItemCart createItemCart(Item item, Integer itemCount) {
        // item과 수량 설정된 itemCart 반환
        ItemCart itemCart = new ItemCart(item, itemCount);
        item.setItemCarts(itemCart); // item쪽에서도 itemCart 설정해주기

        return itemCart;
    }

    public ItemCart(Item item, Integer itemCount) {
        this.item = item;
        this.itemCount = itemCount;
    }

    public void removeCartAndItem() {
        setItem(null);
        setCart(null);
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
