package love.shop.repository.ItemPage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.page.Page;
import love.shop.domain.page.QPage;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemPageRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    QPage page = QPage.page;

    public void savePage(Page page) {
        em.persist(page);
    }

    public Optional<Page> findPageByPageId(Long pageId) {
        return Optional.ofNullable(queryFactory.selectFrom(page)
                .where(page.id.eq(pageId))
                .fetchOne());
    }

    // 사용자가 어떤 아이템을 찾고 있을때
    // 해당 아이템을 가진 페이지를 찾아주면 된다.
    // 어떤 카테고리를 가진 아이템을 찾는다?
    // 어떤 이름을 가진 아이템을 찾는다?





}