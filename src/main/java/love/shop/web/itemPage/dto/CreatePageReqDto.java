package love.shop.web.itemPage.dto;

import lombok.Data;
import love.shop.web.itemOption.dto.CreateItemOptionReqDto;

import java.util.List;
import java.util.Map;

@Data
public class CreatePageReqDto {
    private String pageName;
    private List<String> images;
    private String description;

    private List<CreateItemOptionReqDto> optionList;






}
