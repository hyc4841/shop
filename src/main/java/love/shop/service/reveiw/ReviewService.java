package love.shop.service.reveiw;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.member.Member;
import love.shop.domain.order.Order;
import love.shop.domain.review.Review;
import love.shop.domain.salesPage.SalesPage;
import love.shop.repository.ItemPage.SalesPageRepository;
import love.shop.repository.member.MemberRepository;
import love.shop.repository.order.OrderRepository;
import love.shop.repository.review.ReviewRepository;
import love.shop.web.reveiw.SaveReviewReqDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Review saveReview(SaveReviewReqDto saveReviewDto) {
        Member member = memberRepository.findMemberById(saveReviewDto.getMemberId()); // 작성자
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
}
