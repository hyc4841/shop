package love.shop.domain.delivery;

import jakarta.persistence.*;
import lombok.Getter;
import love.shop.domain.order.Order;

@Entity
@Getter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;


    public void setOrder(Order order) {
        this.order = order;
    }
}
