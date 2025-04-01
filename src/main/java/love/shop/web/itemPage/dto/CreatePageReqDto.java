package love.shop.web.itemPage.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreatePageReqDto {
    private Map<String, List<Long>> optionAndItem;

    private String pageName;
    private List<String> images;
    private String description;
}
