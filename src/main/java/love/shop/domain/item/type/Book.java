package love.shop.domain.item.type;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.ItemCategory.ItemCategory;
import love.shop.domain.item.Item;
import love.shop.web.item.saveDto.BookSaveReqDto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {

    private String author;      // 저자
    private String isbn;        // 도서번호

    private final String dataType = "Book";

    public static Book createBook(String author, String isbn, String name, int price, int stockQuantity, ItemCategory... itemCategories) {
        Book book = new Book(author, isbn, name, price, stockQuantity);
        for (ItemCategory itemCategory : itemCategories) {
            book.addItemCategory(itemCategory); // 연관관계 메서드로 item에 itemCategory를 설정해준다 마찬가지로 item-category쪽에 item을 설정해준다.
        }
        return book;
    }

    public Book(String author, String isbn, String name, int price, int stockQuantity) {
        super(name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }

    public Book(BookSaveReqDto bookDto) {
        super(bookDto.getName(), bookDto.getPrice(), bookDto.getStockQuantity());
        this.author = bookDto.getAuthor();
        this.isbn = bookDto.getIsbn();
    }

    @Override
    public String getType() {
        return dataType;
    }


}