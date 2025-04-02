package love.shop.repository.ItemPage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.ItemCategory.QItemCategory;
import love.shop.domain.category.QCategory;
import love.shop.domain.item.QItem;
import love.shop.domain.item.type.QBook;
import love.shop.domain.item.type.QLapTop;
import love.shop.domain.itemSalesPage.ItemSalesPage;
import love.shop.domain.itemSalesPage.QItemSalesPage;
import love.shop.domain.salesPage.QSalesPage;
import love.shop.domain.salesPage.SalesPage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SalesPageRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    QSalesPage salesPage = QSalesPage.salesPage;
    QItemSalesPage itemSalesPage = QItemSalesPage.itemSalesPage;
    QCategory category = QCategory.category;
    QItem item = QItem.item;
    QItemCategory itemCategory = QItemCategory.itemCategory;
    QBook book = QBook.book;
    QLapTop lapTop = QLapTop.lapTop;


    public void savePage(SalesPage page) {
        em.persist(page);
    }

    public Optional<SalesPage> findPageByPageId(Long pageId) {
        return Optional.ofNullable(queryFactory.selectFrom(salesPage)
                .where(salesPage.id.eq(pageId))
                .fetchOne());
    }

    public Optional<ItemSalesPage> findItemPageByItemPageId(Long itemPageId) {
        return Optional.ofNullable(queryFactory.selectFrom(itemSalesPage)
                .where(itemSalesPage.id.eq(itemPageId))
                .fetchOne());
    }

    public void deleteItemPage(Long itemPageId) {
        queryFactory.delete(itemSalesPage)
                .where(itemSalesPage.id.eq(itemPageId))
                .execute();
    }

    public List<SalesPage> findPageByItemCategory(Long categoryId, int offset, int limit) {

        return queryFactory.selectFrom(salesPage)
                .join(salesPage.itemSalesPages, itemSalesPage)
                .join(itemSalesPage.item, item)
                .join(item.itemCategories, itemCategory)
                .join(itemCategory.category, category)
                .where(itemCategory.category.id.eq(categoryId))
                .offset(offset)
                .limit(limit)
                .distinct()
                .fetch();
    }





    // 사용자가 어떤 아이템을 찾고 있을때
    // 해당 아이템을 가진 페이지를 찾아주면 된다.
    // 어떤 카테고리를 가진 아이템을 찾는다?
    // 어떤 이름을 가진 아이템을 찾는다?





}