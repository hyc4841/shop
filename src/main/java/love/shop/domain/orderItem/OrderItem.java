package love.shop.domain.orderItem;

import jakarta.persistence.*;
import lombok.Getter;
import love.shop.domain.item.Item;
import love.shop.domain.order.Order;

@Entity
@Getter
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    private Item item;

    private Order order;

    private int orderPrice;
    private int count;

    public void setOrder(Order order) {
        this.order = order;
    }
}
