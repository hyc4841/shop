package love.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import love.shop.domain.item.Item;
import love.shop.domain.category.Category;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "category_item")
public class CategoryItem {

    @Id
    @GeneratedValue
    @Column(name = "category_item_id")
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

}
