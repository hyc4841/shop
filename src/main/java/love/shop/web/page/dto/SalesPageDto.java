package love.shop.web.page.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.itemOption.ItemOption;
import love.shop.domain.salesPage.SalesPage;
import love.shop.web.itemOption.dto.ItemOptionDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class SalesPageDto {

    private Long id;
    private String pageName;
    private List<String> images;
    private String description;
    private List<ItemOptionDto> itemOptionList;


//    private List<ReviewDto> reviews; // 리뷰는 따로 가져오는걸로 하자.

    public SalesPageDto(SalesPage salesPage) {
        this.id = salesPage.getId();
        this.pageName = salesPage.getPageName();
        this.images = salesPage.getImages();
        this.description = salesPage.getDescription();

        // 새로 추가된 판매 페이지 상품 옵션
        this.itemOptionList = ItemOptionDto.makeItemOptionDto(salesPage.getItemOptions());
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
