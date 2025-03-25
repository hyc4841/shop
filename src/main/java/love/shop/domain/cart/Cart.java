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

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<ItemCart> itemCartList = new ArrayList<>(); // 장바구니에 추가한 아이템들

    @OneToOne(fetch = FetchType.LAZY)
    private Member member; // 해당 장바구니 주인.







    private void addItemCart(ItemCart itemCart) {
        this.itemCartList.add(itemCart);
        itemCart.setCart(this);
    }

    public static Cart createCart(ItemCart itemCart, Member member) {
        Cart cart = new Cart();

        // itemCart 설정
            cart.addItemCart(itemCart);
        // member 설정
        cart.setMember(member);
        return cart;
    }

    protected void setMember(Member member) {
        this.member = member;
    }


}
