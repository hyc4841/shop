package love.shop.web.item.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchCond {

    // 검색 조건
    private List<String> categories;
    private String itemName;

    // 가격 범위
    private Integer morePrice; // 이상
    private Integer lessPrice; // 이하가격
    // morePrice 는 lessPrice 보다 클 수 없다

}
