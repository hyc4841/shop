package love.shop.web.itemPage.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ModifyPageReqDto {

    private Long pageId;

    private Map<String, Map<Long, Map<String, Boolean>>> optionAndItem; // 새로 넣을 아이템
    private List<Long> deleteItems; // 제거할 아이템 ItemPage의 id임.

    private String pageName;
    private List<String> images;
    private String description;

}
