package love.shop.web.item.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookSaveReqDto {
    // item
    private String name;
    private Integer price;
    private Integer stockQuantity;

    // book
    private String author;
    private String isbn;
    private List<String> categories;
}
