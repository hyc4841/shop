package love.shop.domain.itemSalesPage;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.item.Item;
import love.shop.domain.salesPage.SalesPage;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemSalesPage {

    @Id
    @GeneratedValue
    @Column(name = "item_sales_page_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_page_id")
    private SalesPage salesPage;

    @Column
    private String optionName; // 해당 페이지에 어떤 옵션의 이름으로 들어 갈건지

    @Column
    private String itemDisplayName; // 옵션에서 보일 상품의 이름

    @Column
    private Boolean isMainItem;

    @Column
    private Integer sequence; // 목록에서 보일 순서

    // 그럼 이제 조금 달라짐. 기존에는 아이템을 검색하는 거였다면 이젠 아이템 페이지를 검색하는 꼴이됨.
    // 페이지를 직접 검색하기 보단. 어떤 아이템을 가진 itemPage를 검색해서 itemPage가 가지고 있는 페이지를 가져오면 될듯.

    public ItemSalesPage(String optionName) {
        this.optionName = optionName;
    }

    // 연관관계 메서드
    // item 연결
    public void setItem(Item item) {
        this.item = item;
        item.setItemPages(this);
    }
    // page 연결
    public void setSalesPage(SalesPage salesPage) {
        this.salesPage = salesPage;
        salesPage.setItemSalesPages(this);
    }

    // page와 item 연관관계 끊어주기
    public void deleteItemPage() {
        this.salesPage.getItemSalesPages().remove(this);
        this.salesPage = null;
        this.item.getItemPages().remove(this);
        this.item = null;
    }

    public void setIsMainItemAndDisplayName(Boolean isMainItem, String itemDisplayName) {
        this.isMainItem = isMainItem;
        this.itemDisplayName = itemDisplayName;
    }

}
