package love.shop.service.reveiw;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.OrderMemberNotMatchException;
import love.shop.common.exception.ReviewModifyTimeOutException;
import love.shop.common.exception.UserNotExistException;
import love.shop.domain.member.Member;
import love.shop.domain.order.Order;
import love.shop.domain.review.Review;
import love.shop.domain.salesPage.SalesPage;
import love.shop.repository.salesPage.SalesPageRepository;
import love.shop.repository.member.MemberRepository;
import love.shop.repository.order.OrderRepository;
import love.shop.repository.review.ReviewRepository;
import love.shop.web.reveiw.ModifyReviewReqDto;
import love.shop.web.reveiw.SaveReviewReqDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final SalesPageRepository salesPageRepository;

    @Transactional
    public Review saveReview(SaveReviewReqDto saveReviewDto, Long memberId) {

        // 현재 로그인중인 유저와 리뷰를 달려는 주문건의 멤버 id가 다르면 리뷰를 달 수 없음.
        if (!Objects.equals(saveReviewDto.getOrderId(), memberId)) {
            throw new OrderMemberNotMatchException();
        }

        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new UserNotExistException()); // 작성자
        Order order = orderRepository.findOne(saveReviewDto.getOrderId());
        SalesPage salesPage = salesPageRepository.findPageByPageId(saveReviewDto.getSalesPageId()).orElseThrow(() -> new RuntimeException());

        Review review = new Review(saveReviewDto.getContent(), saveReviewDto.getImages(), saveReviewDto.getStarRating());
        review.setMember(member);
        review.setOrder(order);
        review.setSalesPage(salesPage);

        reviewRepository.saveReview(review);

        // likes 어떻게 할지 생각해두기

        return review;
    }

    @Transactional
    public Review modifyReview(ModifyReviewReqDto modifyDto, Long memberId) {

        Review review = reviewRepository.findReviewByReviewId(modifyDto.getReviewId()).orElseThrow(() -> new RuntimeException());

        if (!Objects.equals(review.getMember().getId(), memberId)) {
            throw new OrderMemberNotMatchException(); // 이 오류는 지금 주문멤버하고 현재 로그인 멤버가 다를때임. 하나 더 만들까?
        }

        LocalDateTime createdAt = review.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        Duration between = Duration.between(createdAt, now);

        if (between.toMinutes() > 30) {
            // 작성한지 30분 넘어가면 수정 못함.
            throw new ReviewModifyTimeOutException();
        }

        review.modifyReview(modifyDto.getContent(), modifyDto.getImages(), modifyDto.getStarRating());

        return review;
    }

    // 리뷰 페이징으로 가져오기
    public Page<Review> findReviewsBySalesPageId(Long salesPageId, int page, int size) {
        return reviewRepository.findReviewsBySalesPageId(salesPageId, page, size);
    }











}
