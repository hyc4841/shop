package love.shop.repository.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.ItemCategory.QItemCategory;
import love.shop.domain.category.Category;
import love.shop.domain.category.QCategory;
import love.shop.domain.item.Item;
import love.shop.domain.item.QBook;
import love.shop.domain.item.QItem;
import love.shop.web.item.dto.BookUpdateReqDto;
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
    QBook book = QBook.book;


    // 아이템 저장
    public void save(Item item) {
        // controller나 service 에서 item 객체를 만들어서 repository로 보낼 것이다. 이때 id는 데이터베이스에 저장될 때 jpa가 자동으로 부여하는
        // 전략을 사용했기 때문에 id 부분은 비어있을 것. 그것을 보고 신규 아이템 저장으로 판단한다는 뜻인듯.
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item); // merge는 준영속 엔티티를 영속 상태로 바꿀때 사용됨. 객체의 id(식별자)를 가지고 있을때 가능함.
            // merge를 사용할 땐, 모든 필드의 값을 다 넣어줘야한다. 값이 안들어간 곳은 null로 채워진다.
        }
    }
    // 아이템 저장
    public void saveItem(Item item) {
        em.persist(item);
    }

    // id로 단건 조회
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    // 모든 아이템 조회
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

    // 카테고리명으로 카테고리 조회
    public Category findCategoryByName(String categoryName) {
        return em.createQuery("select c from Category c" +
                        " where c.categoryName = :category_name", Category.class)
                .setParameter("category_name", categoryName)
                .getSingleResult();
    }

    // 아이템 조건 검색
    public List<Item> findItemsBySearchCond(SearchCond searchCond, int offset, int limit) {

        BooleanBuilder builder = new BooleanBuilder();

        // 아이템 이름 조건
        if (searchCond.getItemName() != null && !searchCond.getItemName().isBlank()) { // isEmpty? isBlank?
            builder.and(item.name.like("%" + searchCond.getItemName() + "%"));
        }

        // 카테고리 조건
        if (searchCond.getCategories() != null && !searchCond.getCategories().isEmpty()) {
            for (String categoryName : searchCond.getCategories()) {
                builder.or(category.categoryName.eq(categoryName));
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
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    // 책 수정
    public void updateBook(BookUpdateReqDto bookDto) {
        queryFactory.update(book)
                .set(book.name, bookDto.getName())
                .set(book.price, bookDto.getPrice())
                .set(book.stockQuantity, bookDto.getStockQuantity())
                .set(book.author, bookDto.getAuthor())
                .set(book.isbn, bookDto.getIsbn())
                .where(book.id.eq(bookDto.getItemId()))
                .execute();
    }
    // 그냥 욕심내지 말고 약식으로 하자..

    // 아이템 삭제
    public void deleteItem(Long itemId) {
        queryFactory.delete(item)
                .where(item.id.eq(itemId))
                .execute();
    }


}
