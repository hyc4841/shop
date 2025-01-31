package love.shop.domain.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import love.shop.domain.delivery.Delivery;
import love.shop.domain.delivery.DeliveryStatus;
import love.shop.domain.member.Member;
import love.shop.domain.orderItem.OrderItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders") // order는 sql에서 예약어이므로 테이블 이름을 orders로 바꿔준다.
@Getter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 주문 회원

    // 양방향 연관관계? 연관관계 주인은 OrderItem인가?
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>(); // Item하고 ManyToMany로 연결할 수 없으니까 중간에 OrderItem 만들어서 1 : n 으로 연결함.

    @OneToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배송정보

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 연관관계 메서드.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    // 주문 생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직
    // 주문 취소
    public void cancel() {
        if (this.delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // 조회 로직
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getOrderPrice();
        }
        return totalPrice;
    }


}
