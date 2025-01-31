package love.shop.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import love.shop.common.exception.NotEnoughStockException;
import love.shop.domain.CategoryItem;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
public abstract class Item {

    // 아이템은 일단 싱글 테이블로 만들고 추후 수정하는 방향으로 가자

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<CategoryItem> categoryItems = new ArrayList<>();

    protected void setName(String name) {
        this.name = name;
    }

    protected void setPrice(int price) {
        this.price = price;
    }

    protected void setStockQuantity(int quantity) {
        this.stockQuantity = quantity;
    }


    // 비즈니스 로직
    // 수량 추가. 기존 수량에 추가하기
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // 수량 제거
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) { // 남은 수량이 음수이면 수량이 부족한 것이기 때문에 예외 발생
            throw new NotEnoughStockException("수량이 부족합니다.");
        }
        this.stockQuantity = restStock;
    }
}
