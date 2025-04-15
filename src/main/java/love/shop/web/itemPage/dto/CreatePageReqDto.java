package love.shop.web.itemPage.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreatePageReqDto {
    private Map<String, Map<Long, Map<String, Boolean>>> optionAndItem; // 이렇게 하지 말고 이제 아이템 옵션을 새로 구현해서 그 구조에 맞게 할거임
    // Map<Long, String> : <itemId, itemDisplayName>

    // optionItem을 계층으로 만들어서 사용할거임.
    // optionItem 생성에 알맞은 데이터 구조가 들어와야함.

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
