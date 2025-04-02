package love.shop.repository.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.review.QReview;
import love.shop.domain.review.Review;
import love.shop.domain.salesPage.QSalesPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    QReview review = QReview.review;
    QSalesPage salesPage = QSalesPage.salesPage;

    // 리뷰 저장
    public void saveReview(Review review) {
        em.persist(review);
    }

    public Page<Review> findReviewsBySalesPageId(Long salesPageId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<Review> reviews = queryFactory.select(review)
                .from(salesPage)
                .where(salesPage.id.eq(salesPageId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(review.count())
                .from(salesPage)
                .where(salesPage.id.eq(salesPageId))
                .fetchOne();

        return new PageImpl<>(reviews, pageable, total);
    }


























}
