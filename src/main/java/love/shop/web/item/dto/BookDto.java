package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.Book;

import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookDto extends ItemDto {

    private String author;
    private String isbn;

    private String dtyep = "Book";

    public BookDto(Book book) {
        super.setName(book.getName());
        super.setPrice(book.getPrice());
        super.setStockQuantity(book.getStockQuantity());
        super.setItemCategories(book.getItemCategories().stream()
                .map(itemCategory -> new ItemCategoryDto(itemCategory))
                .collect(Collectors.toList()));

        this.author = book.getAuthor();
        this.isbn = book.getIsbn();

    }

}
