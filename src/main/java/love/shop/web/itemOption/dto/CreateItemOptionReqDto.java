package love.shop.web.itemOption.dto;

import lombok.Data;

@Data
public class CreateItemOptionReqDto {

    private String optionName; // 옵션 이름

    private Long itemId; // 이 옵션에 해당하는 아이템

    private Integer optionNum; // 부모 자식관계 알려고 넣은 옵션 번호

    private Integer parentNum; // 부모 옵션 번호

    private Boolean isMainItem; // 판매 페이지에 메인 상품

}
