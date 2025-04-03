package love.shop.web.reveiw;

import lombok.Data;

import java.util.List;

@Data
public class ModifyReviewReqDto {

    private Long reviewId;
    private String content;
    private List<String> images;
    private Integer starRating;

}
