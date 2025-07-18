package love.shop.domain.cartSalesPage;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.cart.Cart;
import love.shop.domain.itemOption.ItemOption;
import love.shop.domain.salesPage.SalesPage;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartSalesPage {

    @Id
    @GeneratedValue
    @Column(name = "cart_sales_page_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_page_id")
    private SalesPage salesPage;

    // 해당 판매 페이지에 어떤 아이템들이 들어가 있는지
    @OneToMany(mappedBy = "cartSalesPage")
    private List<ItemOption> itemOptions = new ArrayList<>();
    // 아니지 그냥 저장할 때부터 그냥 계층으로 넣으면 되지 않나?
}
