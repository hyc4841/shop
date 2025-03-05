package love.shop.domain.delivery;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import love.shop.domain.address.Address;
import love.shop.domain.order.Order;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = LAZY)
    @JoinColumn(name = "address_id") // 연관관계 주인. 외래키 설정
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY : 준비, COMP : 배송

    public Delivery(Address address, DeliveryStatus status) {
        this.address = address;
        this.status = status;
    }

    public static Delivery createDelivery(Address address) {
        return new Delivery(address, DeliveryStatus.PENDING);
    }

    // 배송 취소 메서드
    public void cancelDelivery() {
        status = DeliveryStatus.CANCELLED;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
