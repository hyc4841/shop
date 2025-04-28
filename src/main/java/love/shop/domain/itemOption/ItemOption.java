package love.shop.domain.itemOption;

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

    // 카테고리 처럼

    @Id
    @GeneratedValue
    @Column(name = "item_option_id")
    private Long id;

    private String optionName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "p_id")
    private ItemOption parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ItemOption> child = new ArrayList<>(); // 다중 옵션 이므로 자식을 갖도로 설정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sales_page_id")
    private SalesPage salesPage;

    private Integer optionNum; // 옵션 저장 과정에서 부모 자식 식별용 속성

    private Boolean isMainItem;

    // 최상위 옵션 생성자
    public ItemOption(String optionName, Integer optionNum) {
        this.optionName = optionName;
        this.optionNum = optionNum;
        this.isMainItem = false;
    }

    // 연관관계 메서드
    public void setSalesPage(SalesPage salesPage) {
        this.salesPage = salesPage;
        salesPage.getItemOptions().add(this);
    }

    public void setItem(Item item) {
        this.item = item;
        item.getItemOption().add(this);
    }

    public void setParent(ItemOption itemOption) {
        this.parent = itemOption;
        itemOption.getChild().add(this);
    }

    public void setIsMainItem(Boolean isMainItem) {
        this.isMainItem = isMainItem;
    }
}
