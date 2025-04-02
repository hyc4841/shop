package love.shop.web.page.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.salesPage.SalesPage;
import love.shop.web.itemPage.dto.ItemPageDto;
import love.shop.web.reveiw.ReviewDto;

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

    private List<ItemPageDto> itemPages;

    private List<ReviewDto> reviews;

    public SalesPageDto(SalesPage salesPage) {
        this.id = salesPage.getId();
        this.pageName = salesPage.getPageName();
        this.images = salesPage.getImages();
        this.description = salesPage.getDescription();
        this.itemPages = salesPage.getItemSalesPages().stream()
                .map(itemPage -> new ItemPageDto(itemPage))
                .collect(Collectors.toList());

        this.reviews = salesPage.getReviews().stream()
                .map(review -> new ReviewDto(review))
                .collect(Collectors.toList());
    }

    public static List<SalesPageDto> createPageDtoList(List<SalesPage> pageList) {
        List<SalesPageDto> salesPageDtoList = new ArrayList<>();

        for (SalesPage salesPage : pageList) {
            log.info("지연조회 시점");
            salesPageDtoList.add(new SalesPageDto(salesPage));
        }

        return salesPageDtoList;
    }
}
