package love.shop.web.item.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public abstract class ItemSaveReqDto {

    private String name;
    private int price;
    private int stockQuantity;

    private List<Integer> categoriesId;
    // 제일 베스트는 클라이언트 쪽에서 id로 넘겨주는 건데

    public abstract String getType();

}
