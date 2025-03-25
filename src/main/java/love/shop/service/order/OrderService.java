package love.shop.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.address.Address;
import love.shop.domain.delivery.Delivery;
import love.shop.domain.delivery.DeliveryStatus;
import love.shop.domain.item.Item;
import love.shop.domain.member.Member;
import love.shop.domain.order.Order;
import love.shop.domain.orderItem.OrderItem;
import love.shop.repository.address.AddressRepository;
import love.shop.repository.item.ItemRepository;
import love.shop.repository.member.MemberRepository;
import love.shop.repository.order.OrderRepository;
import love.shop.web.order.dto.OrderItemSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final AddressRepository addressRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, List<OrderItemSet> orderItemSets, Long addressId) {

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemSet orderItemSet : orderItemSets) {
            Item item = itemRepository.findOne(orderItemSet.getItemId()).orElseThrow(); // 주문한 아이템 조회
            OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), orderItemSet.getCount()); // 주문 상품 생성
            orderItems.add(orderItem);
        }

        // 회원이 등록해놓은 주소 조회
        Address address = addressRepository.findAddressById(addressId);
        // 배송정보 생성
        Delivery delivery = new Delivery(address, DeliveryStatus.PENDING);


        // 주문한 멤버 조회
        Member member = memberRepository.findMemberById(memberId);
        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItems); // 현재 로직은 주문 아이템이 하나만 들어간다.

        orderRepository.save(order); // order를 persist하게 되면 order에 id가 생긴다.
        return order.getId();
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }

    // 모든 주문 조회
    public List<Order> findAllOrder() {
        return orderRepository.findAllWithMemberDelivery();
    }

    public List<Order> findAllOrders(int offset, int limit) {
        return orderRepository.findAllWithMemberDelivery(offset, limit);
    }

    public List<Order> findOrdersByMemberId(Long memberId, int offset, int limit) {
        return orderRepository.findOrdersByMemberId(memberId, offset, limit);
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findOne(orderId);
    }



}
