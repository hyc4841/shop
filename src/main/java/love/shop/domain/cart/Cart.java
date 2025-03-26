package love.shop.domain.cart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import love.shop.domain.itemCart.ItemCart;
import love.shop.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart { // 장바구니

    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 해당 장바구니 주인.

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCart> itemCarts = new ArrayList<>(); // 장바구니에 추가한 아이템들

    public void addItemCart(ItemCart itemCart) {
        // 여기로 들어오는 itemCart는 item과 수량이 설정된 객체임(item 쪽에도 itemCart가 설정된 상태)
        // cart쪽 itemCart 설정해주기, itemCart에도 cart 썰정해주기
        this.itemCarts.add(itemCart);
        itemCart.setCart(this);
    }

    public Cart(Member member) {
        this.member = member;
    }

    public void deleteItemCart(ItemCart itemCart) {
        this.itemCarts.remove(itemCart);
    }
}
