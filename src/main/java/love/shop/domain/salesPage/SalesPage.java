package love.shop.domain.salesPage;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.ItemOption;
import love.shop.domain.itemSalesPage.ItemSalesPage;
import love.shop.domain.order.Order;
import love.shop.domain.review.Review;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesPage {

    @Id
    @GeneratedValue
    @Column(name = "sales_page_id")
    private Long id;

    private String pageName;        // 상품 페이지 이름
    private List<String> images;    // 상품 페이지 이미지
    private String description;     // 상품 페이지 설명

    @OneToMany(mappedBy = "salesPage")
    private List<ItemSalesPage> itemSalesPages = new ArrayList<>();

    @OneToMany(mappedBy = "salesPage")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "salesPage")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "salesPage")
    private List<ItemOption> itemOption = new ArrayList<>();

    @Column
    private Boolean pageIsActive;



    //    private String seller;          // 판매자 나중에 연관관계 연결해야함.


    public SalesPage(String pageName, List<String> images, String description) {
        this.pageName = pageName;
        this.images = images;
        this.description = description;
        this.pageIsActive = true;
    }

    public void setItemSalesPages(ItemSalesPage itemSalesPages) {
        this.itemSalesPages.add(itemSalesPages);
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    // page 수정 메서드
    public void modifyPage(String pageName, String description, List<String> images) {
        if (pageName != null) {
            this.pageName = pageName;
        }
        if (description != null) {
            this.description = description;
        }
        if (images != null) {
            this.images = images;
        }
    }

    public void deletePage() {
        this.pageIsActive = false;
    }


}
