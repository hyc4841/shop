package love.shop.web.page.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.item.Item;
import love.shop.domain.itemSalesPage.ItemSalesPage;
import love.shop.domain.salesPage.SalesPage;
import love.shop.web.item.dto.ItemDto;
import love.shop.web.itemPage.dto.ItemSalesPageDto;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
public class SalesPageDto {

    private Long id;
    private String pageName;
    private List<String> images;
    private String description;

    private List<ItemSalesPageDto> itemSalesPages;

    private ItemDto mainItem;

//    private List<ReviewDto> reviews; // 리뷰는 따로 가져오는걸로 하자.

    public SalesPageDto(SalesPage salesPage) {
        this.id = salesPage.getId();
        this.pageName = salesPage.getPageName();
        this.images = salesPage.getImages();
        this.description = salesPage.getDescription();
        this.itemSalesPages = salesPage.getItemSalesPages().stream()
                .map(itemPage -> new ItemSalesPageDto(itemPage))
                .collect(Collectors.toList());

        for (ItemSalesPage itemSalesPage : salesPage.getItemSalesPages()) {
            if (itemSalesPage.getIsMainItem()) {
                this.mainItem = ItemDto.createItemDto(Item.proxyToEntity(itemSalesPage.getItem()));
            }
        }


    }

    public static List<SalesPageDto> createPageDtoList(List<SalesPage> pageList) {
        List<SalesPageDto> salesPageDtoList = new ArrayList<>();

        for (SalesPage salesPage : pageList) {
            log.info("지연조회 시점");
            salesPageDtoList.add(new SalesPageDto(salesPage));
        }

        return salesPageDtoList;
    }

    /*
    private Item proxyToEntity(Object entity) {
        // 프록시 객체면 원본 객체로 바꾸기
        if (entity instanceof HibernateProxy) {
            log.info("프록시 객체면 원본 객체로 바꾸기");
            entity = ((HibernateProxy) entity)
                    .getHibernateLazyInitializer()
                    .getImplementation();
        }

        return (Item) entity;
    }
     */
}
