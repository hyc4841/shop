package love.shop.repository.ItemPage;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.ItemCategory.QItemCategory;
import love.shop.domain.category.QCategory;
import love.shop.domain.item.QItem;
import love.shop.domain.item.type.QLapTop;
import love.shop.domain.item.type.QTv;
import love.shop.domain.itemOption.QItemOption;
import love.shop.domain.itemSalesPage.ItemSalesPage;
import love.shop.domain.itemSalesPage.QItemSalesPage;
import love.shop.domain.salesPage.QSalesPage;
import love.shop.domain.salesPage.SalesPage;
import love.shop.web.item.spec.lapTop.*;
import love.shop.web.item.searchCond.SearchCond;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
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
    QItemOption itemOption = QItemOption.itemOption;


    QLapTop lapTop = QLapTop.lapTop;
    QTv tv = QTv.tv;

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

    // 이거 로직 수정해야함. 지금은 ItemSalesPage가 ItemOption으로 변경됨.
    public List<SalesPage> findSalesPageByItemCategoryAndSearchCond(SearchCond searchCond, Map<String, List<String>> filters, int offset, int limit) {

        JPAQuery<SalesPage> query = queryFactory.selectFrom(salesPage)
                .join(itemOption).on(salesPage.id.eq(itemOption.salesPage.id))
                .join(item).on(item.id.eq(itemOption.item.id))
                .join(itemCategory).on(item.id.eq(itemCategory.item.id))
                .join(category).on(category.id.eq(itemCategory.category.id));

        BooleanBuilder basicCond = new BooleanBuilder();
        BooleanBuilder filterCond = new BooleanBuilder();

        // 상품 이름
        if (searchCond.getItemName() != null && !searchCond.getItemName().isBlank()) { // isEmpty? isBlank?
            basicCond.and(item.name.like("%" + searchCond.getItemName() + "%"));
        }
        // 카테고리
        if (searchCond.getCategories() != null) {
            basicCond.and(category.id.eq(searchCond.getCategories()));
        }
        // 최소 가격
        if (searchCond.getMorePrice() != null) {
            basicCond.and(item.price.goe(searchCond.getMorePrice()));
        }
        // 최대 가격
        if (searchCond.getLessPrice() != null) {
            basicCond.and(item.price.loe(searchCond.getLessPrice()));
        }

        if (searchCond.getType() != null) {
            switch (searchCond.getType()) {
                case "LapTop" :
                    log.info("노트북 변환");
                    lapTopSearchCond(filterCond, filters);
                    query.join(lapTop).on(item.id.eq(lapTop.id));
                    break;
                case "TV" :
                    log.info("TV 변환");
                    TVSearchCond(filterCond, filters);
                    query.join(tv).on(item.id.eq(tv.id));
                    break;

                default:
                    log.info("유효한 카테고리가 없습니다={}", searchCond.getType());
                    break;
            }
        }

        return query
                .where(basicCond.and(filterCond))
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    private void lapTopSearchCond(BooleanBuilder builder, Map<String, List<String>> filters) {

        // 굳이 객체로 바꿔서 검색할 필요가 없다.
        for (String key : filters.keySet()) {
            switch (key) {
                case "lapTopBrands":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String lapTopBrand : filters.get(key)) {
                            builder.or(lapTop.lapTopBrand.eq(LapTopBrand.valueOf(lapTopBrand)));
                        }
                    }
                    break;
                case "lapTopCpus":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String lapTopCpu : filters.get(key)) {
                            builder.or(lapTop.lapTopCpu.eq(LapTopCpu.valueOf(lapTopCpu)));
                        }
                    }
                    break;
                case "lapTopStorages":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String lapTopStorage : filters.get(key)) {
                            builder.or(lapTop.lapTopStorage.eq(LapTopStorage.valueOf(lapTopStorage)));
                        }
                    }
                    break;
                case "lapTopScreenSizes":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String lapTopScreenSize : filters.get(key)) {
                            builder.or(lapTop.lapTopScreenSize.eq(LapTopScreenSize.valueOf(lapTopScreenSize)));
                        }
                    }
                    break;
                case "lapTopManufactureBrands":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String lapTopManufactureBrand : filters.get(key)) {
                            builder.or(lapTop.lapTopManufactureBrand.eq(LapTopManufactureBrand.valueOf(lapTopManufactureBrand)));
                        }
                    }
                    break;
            }
        }
    }


    private void TVSearchCond(BooleanBuilder builder, Map<String, List<String>> filters) {

        for (String key : filters.keySet()) {
            switch (key) {
                case "tvSpeakerOutputs":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String tvSpeakerOutputs : filters.get(key)) {
                            log.info("tvSpeakerOutputs={}", tvSpeakerOutputs);
                            builder.or(tv.tvSpeakerOutputs.eq(tvSpeakerOutputs));
                        }
                    }
                    break;
                case "tvProcessors":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String tvProcessors : filters.get(key)) {
                            log.info("tvProcessors={}", tvProcessors);

                            builder.or(tv.tvProcessors.eq(tvProcessors));
                        }
                    }
                    break;
                case "tvDisplayTypes":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String tvDisplayTypes : filters.get(key)) {
                            log.info("tvDisplayTypes={}", tvDisplayTypes);

                            builder.or(tv.tvDisplayTypes.eq(tvDisplayTypes));
                        }
                    }
                    break;
                case "tvSpeakerChannels":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String tvSpeakerChannels : filters.get(key)) {
                            log.info("tvSpeakerChannels={}", tvSpeakerChannels);

                            builder.or(tv.tvSpeakerChannels.eq(tvSpeakerChannels));
                        }
                    }
                    break;
                case "tvManufacturers":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String tvManufacturers : filters.get(key)) {
                            log.info("tvManufacturers={}", tvManufacturers);

                            builder.or(tv.tvManufacturers.eq(tvManufacturers));
                        }
                    }
                    break;
                case "tvHDRs":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String tvHDRs : filters.get(key)) {
                            log.info("tvHDRs={}", tvHDRs);

                            builder.or(tv.tvHDRs.eq(tvHDRs));
                        }
                    }
                    break;
                case "tvDisplayPanels":
                    if (!filters.get(key).get(0).isBlank()) {

                        for (String tvDisplayPanels : filters.get(key)) {
                            log.info("tvDisplayPanels={}", tvDisplayPanels);

                            builder.or(tv.tvDisplayPanels.eq(tvDisplayPanels));
                        }
                    }
                    break;
                case "tvScreenSizes":
                    if (!filters.get(key).get(0).isBlank()) {

                        for (String tvScreenSizes : filters.get(key)) {
                            log.info("tvScreenSizes={}", tvScreenSizes);

                            builder.or(tv.tvScreenSizes.eq(tvScreenSizes));
                        }
                    }
                    break;
                case "tvRefreshRates":
                    if (!filters.get(key).get(0).isBlank()) {

                        for (String tvRefreshRates : filters.get(key)) {
                            log.info("tvRefreshRates={}", tvRefreshRates);

                            builder.or(tv.tvRefreshRates.eq(tvRefreshRates));
                        }
                    }
                    break;
                case "tvBrands":
                    if (!filters.get(key).get(0).isBlank()) {

                        for (String tvBrands : filters.get(key)) {
                            log.info("tvBrands={}", tvBrands);

                            builder.or(tv.tvBrands.eq(tvBrands));
                        }
                    }
                    break;
                case "tvPictureQualities":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String tvPictureQualities : filters.get(key)) {
                            log.info("tvPictureQualities={}", tvPictureQualities);

                            builder.or(tv.tvPictureQualities.eq(tvPictureQualities));
                        }
                    }
                    break;
                case "tvSounds":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String tvSounds : filters.get(key)) {
                            log.info("tvSounds={}", tvSounds);

                            builder.or(tv.tvSounds.eq(tvSounds));
                        }
                    }
                    break;
                case "tvResolutions":
                    if (!filters.get(key).get(0).isBlank()) {
                        for (String tvResolutions : filters.get(key)) {
                            log.info("tvResolutions={}", tvResolutions);

                            builder.or(tv.tvResolutions.eq(tvResolutions));
                        }
                    }
                    break;


            }
        }
    }




        // 사용자가 어떤 아이템을 찾고 있을때
    // 해당 아이템을 가진 페이지를 찾아주면 된다.
    // 어떤 카테고리를 가진 아이템을 찾는다?
    // 어떤 이름을 가진 아이템을 찾는다?





}