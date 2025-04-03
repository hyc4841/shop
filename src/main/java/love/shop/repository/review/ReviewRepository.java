package love.shop.repository.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.member.QMember;
import love.shop.domain.order.QOrder;
import love.shop.domain.review.QReview;
import love.shop.domain.review.Review;
import love.shop.domain.salesPage.QSalesPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    QReview review = QReview.review;
    QSalesPage salesPage = QSalesPage.salesPage;
    QMember member = QMember.member;
    QOrder order = QOrder.order;

    // 리뷰 저장
    public void saveReview(Review review) {
        em.persist(review);
    }

    public Page<Review> findReviewsBySalesPageId(Long salesPageId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<Review> reviews = queryFactory.select(review)
                .from(review)
                .join(review.salesPage, salesPage)
                .where(salesPage.id.eq(salesPageId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(review.count())
                .from(review)
                .join(review.salesPage, salesPage)
                .where(salesPage.id.eq(salesPageId))
                .fetchOne();

        return new PageImpl<>(reviews, pageable, total);
    }

    public Optional<Review> findReviewByReviewId(Long reviewId) {
        return Optional.ofNullable(queryFactory.selectFrom(review)
                .leftJoin(review.member).fetchJoin()
                .leftJoin(review.order).fetchJoin()
                .leftJoin(review.salesPage).fetchJoin()
                .where(review.id.eq(reviewId))
                .fetchOne());
    }


























}
