package love.shop.web.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderReqDto {

    // 배송 주소. 유저가 직접 입력한 경우
    private String city;
    private String street;
    private String zipcode;
    private String detailedAddress;

    // 배송 주소. 유저가 미리 저장해둔 배송지를 선택한 경우
    private Long addressId;



    private List<OrderItemSet> orderItemSets;
//    private List<Long> itemId;  // 주문 상품
//    private Integer count;      // 주문 개수
    // 위 두 개가 한 묶음이어야 한다.

     // 결재 금액, 결재 방식
}