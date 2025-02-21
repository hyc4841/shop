package love.shop.domain.ItemCategory;

import jakarta.persistence.*;
import lombok.Getter;
import love.shop.domain.item.Item;
import love.shop.domain.category.Category;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "item_category")
public class ItemCategory {

    @Id
    @GeneratedValue
    @Column(name = "item_category_id")
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id") // 외래키 설정
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id") // 외래키 설정
    private Item item;

}
