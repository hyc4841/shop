package love.shop.service.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.UnauthorizedAccessException;
import love.shop.common.exception.UserNotExistException;
import love.shop.domain.cart.Cart;
import love.shop.domain.item.Item;
import love.shop.domain.itemCart.ItemCart;
import love.shop.domain.member.Member;
import love.shop.repository.cart.CartRepository;
import love.shop.repository.member.MemberRepository;
import love.shop.service.item.ItemService;
import love.shop.web.cart.dto.AddItemToCartDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final ItemService itemService;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveCart(Cart cart) {
        cartRepository.saveCart(cart);
    }

    // 회원가입시 해당 멤버의 장바구니 생성
    @Transactional
    public Long createCart(Member member) {
        cartRepository.saveCart(new Cart(member));

        Cart cart = cartRepository.findCartByMemberId(member.getId()).orElseThrow(
                () -> new RuntimeException("해당 멤버의 바구니가 존재하지 않음."));

        return cart.getId();
    }

    // 장바구니 조회
    public Cart findCartByMemberId(Long memberId) {
        return cartRepository.findCartByMemberId(memberId).orElseThrow(() -> new RuntimeException("해당 멤버의 장바구니가 존재하지 않음."));
    }


    @Transactional
    public Cart addItem(AddItemToCartDto addItemToCartDto, Long memberId) {

        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new UserNotExistException());

        // 멤버의 장바구니 가져오기
        Cart cart = member.getCart();
        // 아이템 가져오기
        Item item = itemService.findOne(addItemToCartDto.getItemId());
        // ItemCart 생성
        ItemCart itemCart = ItemCart.createItemCart(item, addItemToCartDto.getCount()); // 아이템쪽에도 itemCart가 설정되어 있음
        // itemCart에 집어넣기
        cart.addItemCart(itemCart);


        cartRepository.saveCart(cart);

        return cart;
    }


    // 장바구니 아이템 삭제
    @Transactional
    public Cart removeCartItem(Long itemCartId, Long memberId) {
        // 2. 멤버로부터 장바구니 찾기
        Cart cart = cartRepository.findCartByMemberId(memberId).orElseThrow(() -> new RuntimeException("해당 장바구니가 존재하지 않음."));
        // 3. itemCart 조회
        ItemCart itemCart = cartRepository.findItemCartByCartItemId(itemCartId).orElseThrow(() -> new RuntimeException("해당 itemCart가 존재하지 않음"));
        // 4. itemCartId로 itemCart 조회, 멤버로 조회한 cart의 id와 itemCart의 CartId가 같은지 비교
        // 맞으면 삭제, 아니면 예외처리
        if (!Objects.equals(cart.getId(), itemCart.getCart().getId())) {
            throw new UnauthorizedAccessException("현재 장바구니 주인과 다른 사람이 접근함. 당신 누구야?");
        }


        cart.deleteItemCart(itemCart); // cart쪽에서 itemCart 끊어주기
        Item item = itemCart.getItem();
        item.deleteItemCart(itemCart);

        // 5. itemCart에서 cart와 item 관계 끊어주기
        itemCart.removeCartAndItem();

        // 6. 해당 itemCart 삭제.
        cartRepository.deleteItemCart(itemCart);

        return cart;
    }

    public Cart findCartByCartId(Long cartId) {
        return cartRepository.findCartByCartId(cartId).orElseThrow(() -> new RuntimeException("해당 장바구니가 존재하지 않음."));
    }

    public ItemCart findItemCartByCartItemId(Long itemCartId) {
        return cartRepository.findItemCartByCartItemId(itemCartId).orElseThrow(() -> new RuntimeException("해당 itemCart가 존재하지 않음"));
    }
}
