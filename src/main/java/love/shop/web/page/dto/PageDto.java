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
public class PageDto {

    private Long id;
    private String pageName;
    private List<String> images;
    private String description;

    private List<ItemPageDto> itemPages;

    private List<ReviewDto> reviews;

    public PageDto(SalesPage page) {
        this.id = page.getId();
        this.pageName = page.getPageName();
        this.images = page.getImages();
        this.description = page.getDescription();
        this.itemPages = page.getItemPages().stream()
                .map(itemPage -> new ItemPageDto(itemPage))
                .collect(Collectors.toList());

        this.reviews = page.getReviews().stream()
                .map(review -> new ReviewDto(review))
                .collect(Collectors.toList());
    }

    public static List<PageDto> createPageDtoList(List<SalesPage> pageList) {
        List<PageDto> pageDtoList = new ArrayList<>();

        for (SalesPage page : pageList) {
            log.info("지연조회 시점");
            pageDtoList.add(new PageDto(page));
        }

        return pageDtoList;
    }
}
