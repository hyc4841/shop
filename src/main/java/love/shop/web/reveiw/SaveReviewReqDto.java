package love.shop.web.reveiw;

import lombok.Data;

import java.util.List;

@Data
public class SaveReviewReqDto {

//    private Long memberId;
    private Long orderId;
    private Long salesPageId;
    private String content;
    private List<String> images;

    private Integer starRating;
}
