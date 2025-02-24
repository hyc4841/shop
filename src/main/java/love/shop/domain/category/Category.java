package love.shop.domain.category;

import jakarta.persistence.*;

import lombok.Getter;
import love.shop.domain.ItemCategory.ItemCategory;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @Column
    private String name; // 카테고리 이름

    // mappedBy = "category"는 주인쪽 category 필드를 참조하고 있다는 뜻
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<ItemCategory> itemCategories = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }
}
