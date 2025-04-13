package love.shop.repository.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    // 2개 이상의 아이템 조회
    public List<Item> findItemsByItemId(List<Long> items) {
        List<Item> itemList = new ArrayList<>();

        for (Long itemId : items) {
            Optional<Item> item = findOne(itemId);
            if (item.isPresent()) {
                itemList.add(item.get());
            }
        }

        return itemList;
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
//                .leftJoin(category.parent).fetchJoin()
                .fetch();
    }

    public List<Category> findMajorCategory() {
        return queryFactory.select(category)
                .from(category)
//                .leftJoin(category.parent).fetchJoin()
                .leftJoin(category.children).fetchJoin()
                .where(category.subCategoryName.eq("Major Category"))
                .fetch();
    }

        // 아이템 조건 검색
    public List<Item> findItemsBySearchCond(SearchCond searchCond, Map<String, List<String>> filters, int offset, int limit) {

        JPAQuery<Item> query = queryFactory.selectFrom(item);
        BooleanBuilder basicCond = new BooleanBuilder();
        BooleanBuilder filterCond = new BooleanBuilder();

        log.info("데이터 타입={}", searchCond.getType());

        // 아이템 이름 조건
        if (searchCond.getItemName() != null && !searchCond.getItemName().isBlank()) { // isEmpty? isBlank?
            basicCond.and(item.name.like("%" + searchCond.getItemName() + "%"));
        }
        /*
        // 카테고리 조건
        if (searchCond.getCategories() != null && !searchCond.getCategories().isEmpty()) {
            for (Long categoryId : searchCond.getCategories()) {
                basicCond.or(category.id.eq(categoryId));
            }
        }
         */
        if (searchCond.getCategories() != null) {
//            basicCond.and(category.id.eq(searchCond.getCategories()));
            basicCond.and(category.id.eq(searchCond.getCategories()));
        }

        // 가격 조건
        if (searchCond.getMorePrice() != null) {
            basicCond.and(item.price.goe(searchCond.getMorePrice()));
        }

        if (searchCond.getLessPrice() != null) {
            basicCond.and(item.price.loe(searchCond.getLessPrice()));
        }

        if (searchCond.getType() != null) {
            switch (searchCond.getType()) {
                case "LapTop" :
                    log.info("노트북 변환중");
                    lapTopSearchCond(filterCond, filters);
                    query.join(lapTop).on(item.id.eq(lapTop.id));

                    break;
                    /*
                case "Book" -> bookScreenSearchCond((BookSearchCond) searchCond, basicCond);
                case "SmartPhone" -> smartPhoneSearchCond((SmartPhoneSearchCond) searchCond, basicCond);
                case "Projector" -> projectorSearchCond((ProjectorSearchCond) searchCond, basicCond);
                case "BeamScreen" -> beamScreenSearchCond((BeamScreenSearchCond) searchCond, basicCond);
                case "StreamingDongle" -> streamingDongleSearchCond((StreamingDongleSearchCond) searchCond, basicCond);
                case "streamingMediaPlayer" -> streamingMediaPlayerSearchCond((StreamingMediaPlayerSearchCond) searchCond, basicCond);
                case "DeskTop" -> deskTopSearchCond((DeskTopSearchCond) searchCond, basicCond);
                case "Monitor" -> monitorSearchCond((MonitorSearchCond) searchCond, basicCond);
                case "MFP" -> mFPSearchCond((MFPSearchCond) searchCond, basicCond);
                case "Printer" -> printerSearchCond((PrinterSearchCond) searchCond, basicCond);
                case "TonerCartridge" -> tonerCartridgeSearchCond((TonerCartridgeSearchCond) searchCond, basicCond);
                case "InkCartridge" -> inkCartridgeSearchCond((InkCartridgeSearchCond) searchCond, basicCond);
                case "Scanner" -> scannerSearchCond((ScannerSearchCond) searchCond, basicCond);
                case "WirelessEarbuds" -> wirelessEarBudsSearchCond((WirelessEarbudsSearchCond) searchCond, basicCond);
                case "WirelessHeadphones" -> wirelessHeadphonesSearchCond((WirelessHeadphonesSearchCond) searchCond, basicCond);
                case "WiredEarbuds" -> wiredEarbudsSearchCond((WiredEarbudsSearchCond) searchCond, basicCond);
                case "WiredHeadphones" -> wiredHeadphonesSearchCond((WiredHeadphonesSearchCond) searchCond, basicCond);
                case "WirelessHeadset" -> wirelessHeadsetSearchCond((WirelessHeadsetSearchCond) searchCond, basicCond);
                case "WiredHeadset" -> wiredHeadsetSearchCond((WiredHeadsetSearchCond) searchCond, basicCond)
                     */
                default:
                    log.info("유효한 카테고리가 없습니다={}", searchCond.getType());
                    break;
            }
        }



        return query
                .join(itemCategory).on(item.id.eq(itemCategory.item.id))
                .join(category).on(itemCategory.category.id.eq(category.id))
                .where(basicCond.and(filterCond))
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    private void lapTopSearchCond(BooleanBuilder builder, Map<String, List<String>> filters) {
        log.info("변환된 필터에 뭐들어오는데?={}", filters);

        // 굳이 객체로 바꿔서 검색할 필요가 없다.
        for (String key : filters.keySet()) {
            log.info("키값={}", key);
            switch (key) {
                case "lapTopBrands":
                    log.info("안들어 있어?");
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String lapTopBrand : filters.get(key)) {
                            log.info("여기 뭐들어오는데?={}", lapTopBrand);
                            builder.or(lapTop.lapTopBrand.eq(LapTopBrand.valueOf(lapTopBrand)));
                        }
                    }
                    break;
                case "lapTopCpus":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String lapTopCpu : filters.get(key)) {
                            log.info("여기 뭐들어오는데?={}", lapTopCpu);
                            builder.or(lapTop.lapTopCpu.eq(LapTopCpu.valueOf(lapTopCpu)));
                        }
                    }
                    break;
                case "lapTopStorages":
                    log.info("비었어?={}", filters.get(key).isEmpty());
                    log.info("크기 몇인데?={}", filters.get(key).size());
                    log.info("그래서 뭐 어떻게 해야하는데?={}", filters.get(key).get(0).isBlank());

                    if (!filters.get(key).get(0).isBlank()) {
                        for (String lapTopStorage : filters.get(key)) {
                            log.info("여기 뭐들어오는데?={}", lapTopStorage);
                            builder.or(lapTop.lapTopStorage.eq(LapTopStorage.valueOf(lapTopStorage)));
                        }
                    }
                    break;
                case "lapTopScreenSizes":
                    log.info("비었어?={}", filters.get(key).isEmpty());
                    log.info("크기 몇인데?={}", filters.get(key).size());

                    if (!filters.get(key).get(0).isBlank()) {
                        for (String lapTopScreenSize : filters.get(key)) {
                            log.info("여기 뭐들어오는데?={}", lapTopScreenSize);
                            builder.or(lapTop.lapTopScreenSize.eq(LapTopScreenSize.valueOf(lapTopScreenSize)));
                        }
                    }
                    break;
                case "lapTopManufactureBrands":
                    log.info("비었어?={}", filters.get(key).isEmpty());
                    log.info("크기 몇인데?={}", filters.get(key).size());
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String lapTopManufactureBrand : filters.get(key)) {
                            log.info("여기 뭐들어오는데?={}", lapTopManufactureBrand);
                            builder.or(lapTop.lapTopManufactureBrand.eq(LapTopManufactureBrand.valueOf(lapTopManufactureBrand)));
                        }
                    }
                    break;
            }
        }
    }

    /*
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
     */

    // 해당 카테고리의 데이터 타입 조회하기
    public Optional<Category> findCategoryType(Long categoryId) {
        return Optional.ofNullable(queryFactory.select(category)
                .from(category)
                .where(category.id.eq(categoryId))
                .fetchOne());
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

    // 카테고리로 아이템 조회
    public List<Item> findItemsByCategoryId(Long categoryId) {
        return queryFactory.select(item)
                .from(itemCategory)
                .join(itemCategory.item)
                .join(itemCategory.category)
                .where(itemCategory.category.id.eq(categoryId))
                .fetch();
    }






}
