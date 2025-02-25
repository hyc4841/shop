package love.shop.repository.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.ItemCategory.QItemCategory;
import love.shop.domain.category.Category;
import love.shop.domain.category.QCategory;
import love.shop.domain.item.Book;
import love.shop.domain.item.Item;
import love.shop.domain.item.QItem;
import love.shop.web.item.dto.SearchCond;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    QCategory category = QCategory.category;
    QItem item = QItem.item;
    QItemCategory itemCategory = QItemCategory.itemCategory;

    // 아이템 저장
    public void save(Item item) {
        // controller나 service 에서 item 객체를 만들어서 repository로 보낼 것이다. 이때 id는 데이터베이스에 저장될 때 jpa가 자동으로 부여하는
        // 전략을 사용했기 때문에 id 부분은 비어있을 것. 그것을 보고 신규 아이템 저장으로 판단한다는 뜻인듯.
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    public void saveItem(Item item) {
        em.persist(item);

    }

    // id로 단건 조회
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    // 아이템 모두 조회
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

    public Category findCategoryByName(String categoryName) {
        return em.createQuery("select c from Category c" +
                        " where c.name = :name", Category.class)
                .setParameter("name", categoryName)
                .getSingleResult();
    }

    public List<Item> findItemsBySearchCond(SearchCond searchCond) {

        BooleanBuilder builder = new BooleanBuilder();

        // 아이템 이름 조건
        if (searchCond.getItemName() != null && !searchCond.getItemName().isBlank()) { // isEmpty? isBlank?
            builder.and(item.name.like("%" + searchCond.getItemName() + "%"));
        }

        // 카테고리 조건
        if (searchCond.getCategories() != null && !searchCond.getCategories().isEmpty()) {
            for (String categoryName : searchCond.getCategories()) {
                builder.or(category.name.eq(categoryName));
            }
        }

        // 가격 조건
        if (searchCond.getMorePrice() != null) {
            builder.and(item.price.goe(searchCond.getMorePrice()));
        }

        if (searchCond.getLessPrice() != null) {
            builder.and(item.price.loe(searchCond.getLessPrice()));
        }

        return queryFactory.select(item)
                .from(itemCategory)
                .join(itemCategory.item, item)
                .join(itemCategory.category, category)
                .where(builder)
                .fetch();
    }
}
