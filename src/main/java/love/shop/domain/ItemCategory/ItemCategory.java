package love.shop.domain.ItemCategory;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.item.Item;
import love.shop.domain.category.Category;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "item_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCategory {

    @Id
    @GeneratedValue
    @Column(name = "item_category_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id") // 외래키 설정
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id") // 외래키 설정
    private Item item;

    public void setItem(Item item) {
        this.item = item;
    }

    public ItemCategory(Category category) {
        this.category = category;
    }

    public static ItemCategory createItemCategory(Category category) {
        return new ItemCategory(category);
    }

}
