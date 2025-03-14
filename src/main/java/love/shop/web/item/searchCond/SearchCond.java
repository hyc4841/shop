package love.shop.web.item.searchCond;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import love.shop.web.item.saveDto.BookSaveReqDto;
import love.shop.web.item.saveDto.LapTopSaveReqDto;

import java.util.List;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LapTopSearchCond.class, name = "LapTop"),
        @JsonSubTypes.Type(value = BookSaveReqDto.class, name = "Book"),
})
@Data
public class SearchCond {

    private String type; // 자식 클래스 이름
    // 검색 조건
    private List<Long> categories;
    private String itemName;

    // 가격 범위
    private Integer morePrice; // 이상
    private Integer lessPrice; // 이하가격
    // morePrice 는 lessPrice 보다 클 수 없다

}
