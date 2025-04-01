package love.shop.domain.item;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.NotEnoughStockException;
import love.shop.domain.ItemCategory.ItemCategory;
import love.shop.domain.item.type.Book;
import love.shop.domain.item.type.LapTop;
import love.shop.domain.itemCart.ItemCart;
import love.shop.domain.itemPage.ItemPage;
import love.shop.web.item.saveDto.BookSaveReqDto;
import love.shop.web.item.saveDto.ItemSaveReqDto;
import love.shop.web.item.saveDto.LapTopSaveReqDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Getter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
    private String itemCode; // 아이템의 코드. 같은 제품이지만 색상 또는 사이즈가 다른 제품이 있을 수도 있기 때문.

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemCategory> itemCategories = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ItemCart> itemCarts = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ItemPage> itemPages = new ArrayList<>();

//    private String seller; 판매자

    // 연관관계 메서드
    // 아이템 - 아이템/카테고리 연결
    public void addItemCategory(ItemCategory itemCategory) {
        itemCategories.add(itemCategory);
        itemCategory.setItem(this);
    }

    public Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
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

    public static Item createItem(ItemSaveReqDto itemDto) {

        return switch (itemDto.getType()) {
            case "LapTop" -> new LapTop((LapTopSaveReqDto) itemDto);
            case "Book" -> new Book((BookSaveReqDto) itemDto);
            case "SmartPhone" -> new Book((BookSaveReqDto) itemDto);
            case "Projector" -> new Book((BookSaveReqDto) itemDto);
            case "BeamScreen" -> new Book((BookSaveReqDto) itemDto);
            case "StreamingDongle" -> new Book((BookSaveReqDto) itemDto);
            case "streamingMediaPlayer" -> new Book((BookSaveReqDto) itemDto);
            case "DeskTop" -> new Book((BookSaveReqDto) itemDto);
            case "Monitor" -> new Book((BookSaveReqDto) itemDto);
            case "MFP" -> new Book((BookSaveReqDto) itemDto);
            case "Printer" -> new Book((BookSaveReqDto) itemDto);
            case "TonerCartridge" -> new Book((BookSaveReqDto) itemDto);
            case "InkCartridge" -> new Book((BookSaveReqDto) itemDto);
            case "Scanner" -> new Book((BookSaveReqDto) itemDto);
            case "WirelessEarbuds" -> new Book((BookSaveReqDto) itemDto);
            case "WirelessHeadphones" -> new Book((BookSaveReqDto) itemDto);
            case "WiredEarbuds" -> new Book((BookSaveReqDto) itemDto);
            case "WiredHeadphones" -> new Book((BookSaveReqDto) itemDto);
            case "WirelessHeadset" -> new Book((BookSaveReqDto) itemDto);
            case "WiredHeadset" -> new Book((BookSaveReqDto) itemDto);
            default -> {
                log.info("카테고리에 없는 데이터 유형");
                yield null;
            }
        };
    }

    public void deleteItemCart(ItemCart itemCart) {
        this.itemCarts.remove(itemCart);
    }

    public abstract String getType();

    public void setItemCarts(ItemCart itemCart) {
        this.itemCarts.add(itemCart);
    }

    public void setItemPages(ItemPage itemPages) {
        this.itemPages.add(itemPages);
    }

}
