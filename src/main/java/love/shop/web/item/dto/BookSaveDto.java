package love.shop.web.item.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookSaveDto {
    private String name;
    private Integer price;
    private Integer stockQuantity;

    private String author;
    private String isbn;

    private List<String> categories;
}
