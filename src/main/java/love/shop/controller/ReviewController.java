package love.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.review.Review;
import love.shop.service.reveiw.ReviewService;
import love.shop.web.reveiw.ReviewDto;
import love.shop.web.reveiw.SaveReviewReqDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity<?> saveReview(@RequestBody SaveReviewReqDto saveDto) {

        // 주문 내역쪽에서 데이터를 가져온다. 해당 주문 멤버 id와 order의 멤버 id가 다르면 리뷰를 달 수 없음
        //

        Review review = reviewService.saveReview(saveDto);
        ReviewDto reviewDto = new ReviewDto(review);

        return ResponseEntity.ok(reviewDto);
    }


}
