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
    @JoinColumn(name = "page_id")
    private SalesPage page; // 구매 페이지

    @Column
    private String content; // 리뷰 내용

    @Column
    private List<String> images; // 이미지 경로

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 작성 날짜와 시간

    @Column
    private Integer likes; // 리뷰 좋아요?

    @Column(name = "star_rating")
    private Integer starRating; // 별점



    // 해당 판매 페이지에서 구매에 대한 리뷰를 남기는 것.

}
