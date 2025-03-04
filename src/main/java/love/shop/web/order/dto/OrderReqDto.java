package love.shop.web.order.dto;

import lombok.Data;

@Data
public class OrderReqDto {

    // 배송정보
    private String city;
    private String street;
    private String zipcode;
    private String detailedAddress;
    // 배송 상태는 컨트롤러 단계에서 준비로 넣는다.

    // 주문 아이템 정보
    private Long itemId;

    // 주문자가 주문 단계에서 새로 입력한 배송지
    private Long addressId;

    private Integer count; // 주문 개수


}
