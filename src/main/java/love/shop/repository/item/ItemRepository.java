package love.shop.repository.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.ItemCategory.QItemCategory;
import love.shop.domain.category.Category;
import love.shop.domain.category.QCategory;
import love.shop.domain.item.*;
import love.shop.domain.item.type.QBook;
import love.shop.domain.item.type.QLapTop;
import love.shop.web.item.filter.lapTop.*;
import love.shop.web.item.searchCond.*;
import love.shop.web.item.updateDto.BookUpdateReqDto;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

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
    QLapTop lapTop = QLapTop.lapTop;


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
    public Optional<Item> findOne(Long id) {
        return Optional.ofNullable(em.find(Item.class, id));
    }

    // 모든 아이템 조회
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

    // 카테고리명으로 카테고리 조회
    public Optional<Category> findCategoryByName(String categoryName) {
        return Optional.ofNullable(em.createQuery("select c from Category c" +
                        " where c.categoryName = :category_name", Category.class)
                .setParameter("category_name", categoryName)
                .getSingleResult());
    }

    public List<Category> findCategoryListByName(String categoryName) {
        return em.createQuery("select c from Category c" +
                        " where c.categoryName = :category_name", Category.class)
                .setParameter("category_name", categoryName)
                .getResultList();
    }

    public Optional<Category> findCategoryById(Long categoryId) {
        return Optional.ofNullable(em.find(Category.class, categoryId));
    }

    // toOne 관계는 페치 조인으로, ToMany 관계는 지연로딩으로 조회하기
    public Optional<Category> findCategoryByNameAndParentName(String categoryName, String parentName) {
        log.info("카테고리 이름과 부모 이름으로 찾기");
        return Optional.ofNullable(queryFactory.select(category)
                .from(category)
                .leftJoin(category.parent).fetchJoin()
                .where(category.categoryName.eq(categoryName))
                .where(category.parent.categoryName.eq(parentName))
                .fetchOne());
    }

    // 모든 카테고리 조회
    public List<Category> findAllCategory() {
        return queryFactory.select(category)
                .from(category)
                .leftJoin(category.parent).fetchJoin()
                .fetch();
    }

    public List<Category> findMajorCategory() {
        return queryFactory.select(category)
                .from(category)
                .leftJoin(category.parent).fetchJoin()
                .where(category.subCategoryName.eq("Major Category"))
                .fetch();
    }

        // 아이템 조건 검색
    public List<Item> findItemsBySearchCond(SearchCond searchCond, int offset, int limit) {

        BooleanBuilder builder = new BooleanBuilder();

        switch (searchCond.getType()) {
            case "LapTop" -> lapTopSearchCond((LapTopSearchCond) searchCond, builder);
            case "Book" -> bookScreenSearchCond((BookSearchCond) searchCond, builder);
            case "SmartPhone" -> smartPhoneSearchCond((SmartPhoneSearchCond) searchCond, builder);
            case "Projector" -> projectorSearchCond((ProjectorSearchCond) searchCond, builder);
            case "BeamScreen" -> beamScreenSearchCond((BeamScreenSearchCond) searchCond, builder);
            case "StreamingDongle" -> streamingDongleSearchCond((StreamingDongleSearchCond) searchCond, builder);
            case "streamingMediaPlayer" -> streamingMediaPlayerSearchCond((StreamingMediaPlayerSearchCond) searchCond, builder);
            case "DeskTop" -> deskTopSearchCond((DeskTopSearchCond) searchCond, builder);
            case "Monitor" -> monitorSearchCond((MonitorSearchCond) searchCond, builder);
            case "MFP" -> mFPSearchCond((MFPSearchCond) searchCond, builder);
            case "Printer" -> printerSearchCond((PrinterSearchCond) searchCond, builder);
            case "TonerCartridge" -> tonerCartridgeSearchCond((TonerCartridgeSearchCond) searchCond, builder);
            case "InkCartridge" -> inkCartridgeSearchCond((InkCartridgeSearchCond) searchCond, builder);
            case "Scanner" -> scannerSearchCond((ScannerSearchCond) searchCond, builder);
            case "WirelessEarbuds" -> wirelessEarBudsSearchCond((WirelessEarbudsSearchCond) searchCond, builder);
            case "WirelessHeadphones" -> wirelessHeadphonesSearchCond((WirelessHeadphonesSearchCond) searchCond, builder);
            case "WiredEarbuds" -> wiredEarbudsSearchCond((WiredEarbudsSearchCond) searchCond, builder);
            case "WiredHeadphones" -> wiredHeadphonesSearchCond((WiredHeadphonesSearchCond) searchCond, builder);
            case "WirelessHeadset" -> wirelessHeadsetSearchCond((WirelessHeadsetSearchCond) searchCond, builder);
            case "WiredHeadset" -> wiredHeadsetSearchCond((WiredHeadsetSearchCond) searchCond, builder);

            default -> {
                log.info("유효한 카테고리가 없습니다={}", searchCond.getType());
            }
        }

        // 아이템 이름 조건
        if (searchCond.getItemName() != null && !searchCond.getItemName().isBlank()) { // isEmpty? isBlank?
            builder.and(item.name.like("%" + searchCond.getItemName() + "%"));
        }

        // 카테고리 조건
        if (searchCond.getCategories() != null && !searchCond.getCategories().isEmpty()) {
            for (Long categoryId : searchCond.getCategories()) {
                builder.or(category.id.eq(categoryId));
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

    private void lapTopSearchCond(LapTopSearchCond searchCond, BooleanBuilder builder) {
        if (searchCond.getLapTopBrand() != null) {
            builder.and(lapTop.lapTopBrand.eq(LapTopBrand.valueOf(searchCond.getLapTopBrand())));
        }
        if (searchCond.getLapTopCpu() != null) {
            builder.and(lapTop.lapTopCpu.eq(LapTopCpu.valueOf(searchCond.getLapTopCpu())));
        }
        if (searchCond.getLapTopStorage() != null) {
            builder.and(lapTop.lapTopStorage.eq(LapTopStorage.valueOf(searchCond.getLapTopStorage())));
        }
        if (searchCond.getLapTopScreenSize() != null) {
            builder.and(lapTop.lapTopScreenSize.eq(LapTopScreenSize.valueOf(searchCond.getLapTopStorage())));
        }
        if (searchCond.getLapTopManufactureBrand() != null) {
            builder.and(lapTop.lapTopManufactureBrand.eq(LapTopManufactureBrand.valueOf(searchCond.getLapTopManufactureBrand())));
        }
    }

    private void bookScreenSearchCond(BookSearchCond searchCond, BooleanBuilder builder) {

    }
    private void beamScreenSearchCond(BeamScreenSearchCond searchCond, BooleanBuilder builder) {

    }
    private void deskTopSearchCond(DeskTopSearchCond searchCond, BooleanBuilder builder) {

    }
    private void inkCartridgeSearchCond(InkCartridgeSearchCond searchCond, BooleanBuilder builder) {

    }
    private void mFPSearchCond(MFPSearchCond searchCond, BooleanBuilder builder) {

    }
    private void monitorSearchCond(MonitorSearchCond searchCond, BooleanBuilder builder) {

    }
    private void printerSearchCond(PrinterSearchCond searchCond, BooleanBuilder builder) {

    }
    private void projectorSearchCond(ProjectorSearchCond searchCond, BooleanBuilder builder) {

    }
    private void scannerSearchCond(ScannerSearchCond searchCond, BooleanBuilder builder) {

    }
    private void smartPhoneSearchCond(SmartPhoneSearchCond searchCond, BooleanBuilder builder) {

    }
    private void streamingDongleSearchCond(StreamingDongleSearchCond searchCond, BooleanBuilder builder) {

    }
    private void streamingMediaPlayerSearchCond(StreamingMediaPlayerSearchCond searchCond, BooleanBuilder builder) {

    }
    private void tonerCartridgeSearchCond(TonerCartridgeSearchCond searchCond, BooleanBuilder builder) {

    }
    private void wiredEarbudsSearchCond(WiredEarbudsSearchCond searchCond, BooleanBuilder builder) {

    }
    private void wiredHeadphonesSearchCond(WiredHeadphonesSearchCond searchCond, BooleanBuilder builder) {

    }
    private void wiredHeadsetSearchCond(WiredHeadsetSearchCond searchCond, BooleanBuilder builder) {

    }
    private void wirelessEarBudsSearchCond(WirelessEarbudsSearchCond searchCond, BooleanBuilder builder) {

    }
    private void wirelessHeadphonesSearchCond(WirelessHeadphonesSearchCond searchCond, BooleanBuilder builder) {

    }
    private void wirelessHeadsetSearchCond(WirelessHeadsetSearchCond searchCond, BooleanBuilder builder) {

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


    // 카테고리로 아이템 조회하기
    public List<Item> findItemsByCategories(List<String> categories) {
        BooleanBuilder builder = new BooleanBuilder();

        for (String categoryName : categories) {
            builder.or(itemCategory.category.categoryName.eq(categoryName));
        }

        return queryFactory.select(item)
                .from(itemCategory)
                .join(itemCategory.item)
                .join(itemCategory.category)
                .where(builder)
                .fetch();
    }

    public List<Item> findItemsByCategoryId(Long categoryId) {
        return queryFactory.select(item)
                .from(itemCategory)
                .join(itemCategory.item)
                .join(itemCategory.category)
                .where(itemCategory.category.id.eq(categoryId))
                .fetch();
    }

}
