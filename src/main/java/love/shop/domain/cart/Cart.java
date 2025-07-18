package love.shop.domain.cart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import love.shop.domain.itemCart.ItemCart;
import love.shop.domain.member.Member;
import love.shop.domain.salesPage.SalesPage;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member; // 해당 장바구니 주인.



    /*
    @OneToMany(mappedBy = "cart")
    private List<SalesPage> salesPages; // 해당 페이지에서 선택한 아템.

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCart> itemCarts = new ArrayList<>(); // 장바구니에 추가한 아이템들
     */

}
