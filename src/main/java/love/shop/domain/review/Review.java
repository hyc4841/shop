package love.shop.domain.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.member.Member;
import love.shop.domain.order.Order;
import love.shop.domain.salesPage.SalesPage;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; // 구매내역

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_page_id")
    private SalesPage salesPage; // 구매 페이지

    @Column
    private String content; // 리뷰 내용

    @Column
    private List<String> images; // 이미지 경로

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 작성 날짜와 시간

    @Column(name = "modify_at")
    private LocalDateTime modifyAt; // 수정 시간

    @Column
    private Integer likes; // 리뷰 좋아요?

    @Column(name = "star_rating")
    private Integer starRating; // 별점


    public Review(String content, List<String> images, Integer starRating) {
        this.content = content;
        this.images = images;
        this.createdAt = LocalDateTime.now();
        this.starRating = starRating;
    }

    // 멤버 연결
    public void setMember(Member member) {
        this.member = member;
        member.addReview(this);
    }

    // order 연결
    public void setOrder(Order order) {
        this.order = order;
        order.setReview(this);
    }

    // salesPage 연결
    public void setSalesPage(SalesPage salesPage) {
        this.salesPage = salesPage;
        salesPage.addReview(this);
    }
    // 해당 판매 페이지에서 구매에 대한 리뷰를 남기는 것.


    public void modifyReview(String content, List<String> images, Integer starRating) {
        if (content != null) this.content = content;
        if (images != null) this.images = images;
        if (starRating != null) this.starRating = starRating;
    }




}
