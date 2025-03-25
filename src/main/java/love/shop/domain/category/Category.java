package love.shop.domain.category;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.ItemCategory.ItemCategory;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    // 자기 자신을 부모 관계 참조한다. 하나의 부모는 여러개의 자식을 가질 수 있고, 자식은 하나의 부모만을 갖는다. 따라서 ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

    @Column(name = "category_name")
    private String categoryName; // 카테고리 이름

    @Column(name = "sub_category_name")
    private String subCategoryName; // 해당 계층에서 분류되는 카테고리 이름

    @Column
    private String type;

    @Column
    private Integer sequence; // 해당 분류 카테고리 안에서의 순서

    // mappedBy = "category"는 주인쪽 category 필드를 참조하고 있다는 뜻
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<ItemCategory> itemCategories = new ArrayList<>();


    public Category(String categoryName, String subCategoryName) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
    }

    public Category(String categoryName, String subCategoryName, Integer sequence) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.sequence = sequence;
    }


    public Category(String categoryName, String subCategoryName, String type) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.type = type;
    }

    public Category(String categoryName, String subCategoryName, String type, Integer sequence) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.type = type;
        this.sequence = sequence;
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(String categoryName, Integer sequence) {
        this.categoryName = categoryName;
        this.sequence = sequence;
    }

    public void addItemCategory(ItemCategory itemCategory) {
        itemCategories.add(itemCategory);
    }

    public void addChild(Category child) {
        // 자식 카테고리 추가할 때 이미 들어가 있는 카테고리면 넣지 않는다.
        if (this.children.stream().anyMatch(category -> category.getId().equals(child.getId()))) {
            return;
        }

        this.children.add(child);
        child.setParent(this);
    }

    public void removeChild(Category child) {
        this.children.remove(child);
        child.setParent(null);
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
}
