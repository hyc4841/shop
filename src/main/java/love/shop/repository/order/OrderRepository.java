package love.shop.repository.order;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.order.Order;
import love.shop.web.order.dto.OrdersResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    // 엔티티 -> DTO, fetch join 사용하여 최적화. 최적화는 대부분 이것으로 해결된다고 함.
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .getResultList();
    }

    // DTO로 바로 조회.
    public List<OrdersResponseDto> findOrdersDtos() {
        return em.createQuery(
                "select new love.shop.web.order.dto.OrdersResponseDto(" +
                        "o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrdersResponseDto.class)
                .getResultList();
    }

}
