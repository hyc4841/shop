package love.shop.domain.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders") // order는 sql에서 예약어이므로 테이블 이름을 orders로 바꿔준다.
@Getter @Setter
public class Order {

    @Id
    @Column(name = "order_id")
    private String id;


}
