package love.shop.web.item.saveDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookSaveReqDto extends ItemSaveReqDto {

    private String author;
    private String isbn;
    private List<String> categories;


}
