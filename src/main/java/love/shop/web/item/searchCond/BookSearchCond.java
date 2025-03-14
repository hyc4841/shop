package love.shop.web.item.searchCond;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonTypeName(value = "Book")
@EqualsAndHashCode(callSuper = true)
@Data
public class BookSearchCond extends SearchCond {

    private String author;
    private String isbn;

}