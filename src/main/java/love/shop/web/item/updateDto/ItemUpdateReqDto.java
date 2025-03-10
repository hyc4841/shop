package love.shop.web.item.updateDto;

import lombok.Data;

@Data
public class ItemUpdateReqDto {

    private Long itemId;
    private String name;
    private Integer price;
    private Integer StockQuantity;

}
