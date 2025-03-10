package love.shop.web.item.updateDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookUpdateReqDto extends ItemUpdateReqDto{

    private String author;
    private String isbn;

}
