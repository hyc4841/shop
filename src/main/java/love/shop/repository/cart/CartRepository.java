package love.shop.repository.cart;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.cart.Cart;
import love.shop.domain.cart.QCart;
import love.shop.domain.item.QItem;
import love.shop.domain.itemCart.ItemCart;
import love.shop.domain.itemCart.QItemCart;
import love.shop.domain.member.QMember;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    /*
    QCart cart = QCart.cart;
    QItemCart itemCart = QItemCart.itemCart;
    QItem item = QItem.item;
    QMember member = QMember.member;


    // 장바구니 저장. 장바구니는 최초로 해당 멤버걸로 저장하고 다음부터는 이걸 재활용 해야하지 않나?
    public void saveCart(Cart cart) {
        em.persist(cart);
    }

    // 장바구니 조회
    public Optional<Cart> findCartByMemberId(Long memberId) {
        return Optional.ofNullable(queryFactory.selectFrom(cart)
                .leftJoin(cart.member, member)
                .leftJoin(cart.itemCarts, itemCart).fetchJoin()
                .leftJoin(itemCart.item, item).fetchJoin()
                .where(cart.member.id.eq(memberId))
                .fetchOne());
    }

    // cartId로 장바구니 조회
    public Optional<Cart> findCartByCartId(Long cartId) {
        return Optional.ofNullable(em.find(Cart.class, cartId));
    }

    // itemCartId로 itemCart 조회
    public Optional<ItemCart> findItemCartByCartItemId(Long itemCartId) {
        return Optional.ofNullable(queryFactory.selectFrom(itemCart)
                .where(itemCart.id.eq(itemCartId))
                .fetchOne());
    }

    public void deleteItemCart(ItemCart itemCart) {
        em.remove(itemCart);
    }

     */
}
