package love.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.review.Review;
import love.shop.service.reveiw.ReviewService;
import love.shop.web.login.dto.CustomUser;
import love.shop.web.reveiw.ModifyReviewReqDto;
import love.shop.web.reveiw.ReviewDto;
import love.shop.web.reveiw.SaveReviewReqDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity<?> saveReview(@RequestBody SaveReviewReqDto saveDto) {

        // 로그인한 유저만 가능
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기

        Review review = reviewService.saveReview(saveDto, memberId);
        ReviewDto reviewDto = new ReviewDto(review);

        return ResponseEntity.ok(reviewDto);
    }

    // 리뷰를 수정할 수 있게 해야하나?
    @PatchMapping("/review")
    public ResponseEntity<?> modifyReview(@RequestBody ModifyReviewReqDto modifyDto) {

        // 작성후 30분 안에 수정하면 가능하게?
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기

        Review review = reviewService.modifyReview(modifyDto, memberId);
        ReviewDto reviewDto = new ReviewDto(review);

        return ResponseEntity.ok(reviewDto);
    }














}
