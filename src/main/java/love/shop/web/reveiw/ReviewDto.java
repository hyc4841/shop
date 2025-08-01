package love.shop.web.reveiw;

import lombok.Data;
import love.shop.domain.review.Review;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewDto {

    private Long id;

    private Long memberId;
    private Long orderId;
    private Long salesPageId;
    private String content;
    private List<String> images;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
    private Integer likes;
    private Integer starRating;

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.memberId = review.getMember().getId();
        this.orderId = review.getOrder().getId();
        this.salesPageId = review.getSalesPage().getId();
        this.content = review.getContent();
        this.images = review.getImages();
        this.createAt = review.getCreatedAt();
        this.modifyAt = review.getModifyAt();
        this.likes = review.getLikes();
        this.starRating = review.getStarRating();

    }

}
