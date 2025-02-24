package love.shop.domain.item;

import jakarta.persistence.Entity;
import lombok.Getter;
import love.shop.domain.ItemCategory.ItemCategory;

@Entity
@Getter
public class Book extends Item{

    private String author;
    private String isbn; // 도서번호

    public Book(String author, String isbn, String name, int price, int stockQuantity, ItemCategory... itemCategories) {
        super(name, price, stockQuantity, itemCategories);
        this.author = author;
        this.isbn = isbn;
    }


}