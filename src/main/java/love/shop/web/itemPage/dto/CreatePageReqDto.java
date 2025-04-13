package love.shop.web.itemPage.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreatePageReqDto {
    private Map<String, Map<Long, Map<String, Boolean>>> optionAndItem;
    // Map<Long, String> : <itemId, itemDisplayName>

    /*
        "옵션 이름": {
            {
                "아이템id": {
                    "상품표시이름": 메인상품 표시
                },
            },
        },

     */

    private String pageName;
    private List<String> images;
    private String description;
}
