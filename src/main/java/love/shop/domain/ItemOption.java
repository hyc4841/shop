package love.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.item.Item;
import love.shop.domain.salesPage.SalesPage;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemOption { // 판매 페이지의 계층형 상품 옵션

    @Id
    @GeneratedValue
    @Column(name = "item_option_id")
    private Long id;

    private String optionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_id")
    private ItemOption parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ItemOption> child = new ArrayList<>(); // 다중 옵션 이므로 자식을 갖도로 설정

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_page_id")
    private SalesPage salesPage;

}
