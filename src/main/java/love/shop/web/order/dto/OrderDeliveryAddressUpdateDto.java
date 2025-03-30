package love.shop.web.order.dto;

import lombok.Data;

@Data
public class OrderDeliveryAddressUpdateDto {

    private Long addressId; // 주문자가 미리 저장한 주소를 선택한 경우

    // 직접 입력한 경우
    private String city;
    private String street;
    private String zipcode;
    private String detailedAddress;

}
