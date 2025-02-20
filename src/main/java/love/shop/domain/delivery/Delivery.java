package love.shop.domain.delivery;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import love.shop.domain.Address;
import love.shop.domain.order.Order;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column
    private String city;
    @Column
    private String street;
    @Column
    private String zipcode;
    @Column
    private String detailedAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY : 준비, COMP : 배송



    public void setOrder(Order order) {
        this.order = order;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
