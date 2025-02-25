package love.shop.domain.orderItem;

import jakarta.persistence.*;
import lombok.Getter;
import love.shop.domain.item.Item;
import love.shop.domain.order.Order;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  // 주문 상품.

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setCount(int count) {
        this.count = count;
    }

    // 주문 아이템 생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        item.removeStock(count); // 상품을 count 만큼 주문했으니까 아이템에서는 count 만큼 빼줘야한다.
        return orderItem;
    }

    // 비즈니스 로직
    // 주문 취소. 아이템 수량을 원상태로 복구 시킨다.
    public void cancel() {
        this.getItem().addStock(count);
    }

    // 조회 로직
    // 주문 전체 가격 조회
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
