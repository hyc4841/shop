package love.shop.web.item.dto;

import lombok.Data;

@Data
public class ItemUpdateReqDto {

    private Long itemId;
    private String name;
    private Integer price;
    private Integer StockQuantity;

}
