package love.shop.domain.itemReview;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.item.Item;
import love.shop.domain.member.Member;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "item_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemReview {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_review_id")
    private Long id;
    @Column
    private String content; // 리뷰 내용
    @Column
    private List<String> image; // 이미지 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") // 외래키 설정
    private Item item; // 리뷰 상품. 해당 리뷰에

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 작성 날짜와 시간
    @Column
    private Integer likes; // 리뷰 좋아요?
    @Column(name = "is_repurchased")
    private Boolean isRepurchased; // 재구매 여부. 재구매 리뷰인지 아닌지
    @Column(name = "one_month")
    private Boolean oneMonth; // 한달 사용 리뷰 여부.
    @Column(name = "star_rating")
    private int starRating; // 별점

}
