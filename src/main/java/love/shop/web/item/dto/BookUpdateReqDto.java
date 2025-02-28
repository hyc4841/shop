package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookUpdateReqDto extends ItemUpdateReqDto{

    private String author;
    private String isbn;

}
