package love.shop.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.review.Review;
import love.shop.service.reveiw.ReviewService;
import love.shop.web.login.dto.CustomUser;
import love.shop.web.reveiw.ModifyReviewReqDto;
import love.shop.web.reveiw.ReviewDto;
import love.shop.web.reveiw.SaveReviewReqDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

        // 리뷰는 작성 후 30분이 지나면 못바꾼다. 이건 나중에 리뷰 수정 규칙을 더 자세히 알아보자
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기

        Review review = reviewService.modifyReview(modifyDto, memberId);
        ReviewDto reviewDto = new ReviewDto(review);

        return ResponseEntity.ok(reviewDto);
    }

    // 해당 페이지 리뷰 페이징
    @GetMapping("/review/{salesPageId}")
    public ResponseEntity<?> getReviews(@PathVariable Long salesPageId,
                                        @RequestParam(name = "offset", defaultValue = "0") int offset,
                                        @RequestParam(name = "limit", defaultValue = "10") int limit) {

        // 일단 간단하게 구현 성공, 더미데이터 가득 넣어서 실험해봐야할듯.

        Page<Review> reviews = reviewService.findReviewsBySalesPageId(salesPageId, offset, limit);

        List<ReviewDto> reviewList = reviews.stream()
                                    .map(review -> new ReviewDto(review))
                                    .collect(Collectors.toList());

        ReviewResultWrapper<Object> result = new ReviewResultWrapper<>(reviewList, reviews.getTotalPages(), reviews.getPageable().getPageNumber(),
                reviews.getPageable().getPageSize());

        return ResponseEntity.ok(result);
    }

    @Data
    @AllArgsConstructor
    static class ReviewResultWrapper<T> {
        private T reviews;
        private T totalPage;
        private T page;
        private T size;
    }














}
