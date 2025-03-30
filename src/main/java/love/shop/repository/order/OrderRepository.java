package love.shop.repository.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.address.QAddress;
import love.shop.domain.delivery.QDelivery;
import love.shop.domain.order.Order;
import love.shop.domain.order.QOrder;
import org.springframework.stereotype.Repository;

import java.util.List;

import static love.shop.domain.member.QMember.member;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    QOrder order = QOrder.order;
    QAddress address = QAddress.address;
    QDelivery delivery = QDelivery.delivery;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return queryFactory.select(order)
                .from(order)
                .leftJoin(order.member).fetchJoin()
                .leftJoin(order.delivery).fetchJoin()
                .where(order.id.eq(id))
                .fetchOne();
    }


    // 컬렉션 조회 X. 지연 로딩 최적회를 위한 fetch join
    // 엔티티 -> DTO, fetch join 사용하여 최적화. 최적화는 대부분 이것으로 해결된다고 함.
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .getResultList();
    }

    public void updateOrderDeliveryAddress(Long deliveryId ,Long addressId) {
        queryFactory.update(delivery)
                .where(delivery.id.eq(deliveryId))
                .set(delivery.address.id, addressId)
                .execute();
    }

    // DTO로 바로 조회.
    /*
    public List<OrdersResponseDto> findOrdersDtos() {
        return em.createQuery(
                "select new love.shop.web.order.dto.OrdersResponseDto(" +
                        "o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrdersResponseDto.class)
                .getResultList();
    }
     */

    // 컬렉션 페치 조인. 페이징 불가, 컬렉션 하나만 있을 때 사용. 사실상 실무에선 사용 불가
    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
                .getResultList();
    }

    // 컬렉션이 있는 엔티티 즉, ToMany 관계가 있는 엔티티에서 페이징과 성능 최적화 둘 다 챙기는 방법
    // 먼저 ToOne 관계는 fetch join 으로 조회
    // ToMany 즉, 컬렉션은 지연 로딩으로 조회
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }


    // 이렇게하면 jpa에서 성능 최적화한거 똑같이 할 수 있음. 대박임 개쉬움 굿
    public List<Order> findAllOrders(int offset, int limit) {
        List<Order> orders = queryFactory.selectFrom(order)
                .leftJoin(order.member).fetchJoin()
                .leftJoin(order.delivery).fetchJoin()
                .offset(offset)
                .limit(limit)
                .fetch();

        return orders;
    }

    // 멤버 id로 주문 조회
    public List<Order> findOrdersByMemberId(Long memberId, int offset, int limit) {
        List<Order> orders = queryFactory.selectFrom(order)
                .leftJoin(order.member).fetchJoin()
                .leftJoin(order.delivery).fetchJoin()
                .where(order.member.id.eq(memberId))
                .offset(offset)
                .limit(limit)
                .fetch();

        return orders;
    }




}
