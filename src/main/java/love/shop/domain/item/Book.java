package love.shop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
public class Book extends Item{

    private String author;
    private String isbn;

    public Book(String bookName, int price, int quantity) {
        super.setName(bookName);
        super.setPrice(price);
        super.setStockQuantity(quantity);
    }

    public Book() {
    }
}