package love.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.cart.Cart;
import love.shop.domain.member.Member;
import love.shop.service.cart.CartService;
import love.shop.service.item.ItemService;
import love.shop.service.member.MemberService;
import love.shop.web.cart.dto.AddItemToCartDto;
import love.shop.web.cart.dto.CartDto;
import love.shop.web.login.dto.CustomUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;

    // 장바구니에 상품 추가
    @PostMapping("/cart")
    public ResponseEntity<?> addItem(@RequestBody AddItemToCartDto addItemToCartDto) {
        // 추후에 현재 아이템 수량보다 담는 수량이 많으면 안된다는 로직도 만들기
        log.info("장바구니 추가 아이템={}", addItemToCartDto);

        // 현재 로그인 중인 멤버
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기
        Member member = memberService.findMemberById(memberId);

        Cart cart = cartService.addItem(addItemToCartDto, member);
        CartDto cartDto = new CartDto(cart);

        return ResponseEntity.ok(cartDto);
    }

    // 장바구니에 상품 제거
    @DeleteMapping("/cart")
    public ResponseEntity<?> removeItem(@RequestParam Long itemCartId) {
        log.info("장바구니 상품 제거");

        // 1. 현재 멤버 id 찾아오기
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기

        // cascade 설정해둬서 itemCart만 삭제하면 알아서 cart하고 item에서 삭제되도록 하면 훨씬 쉽다.
        Cart cart = cartService.removeCartItem(itemCartId, memberId);

        return ResponseEntity.ok(new CartDto(cart));
    }

    @PatchMapping("/cart")
    public ResponseEntity<?> modifyItemCount() {
        log.info("장바구니 아이템 수량 변경");

        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기

        Cart cart = cartService.findCartByMemberId(memberId);
        CartDto cartDto = new CartDto(cart);

        return ResponseEntity.ok(cartDto);
    }

    @GetMapping("/cart")
    public ResponseEntity<?> getCart() {
        log.info("장바구니 조회");
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기

        Cart cart = cartService.findCartByMemberId(memberId);

        log.info("조회한 장바구니={}", cart);

        return ResponseEntity.ok(new CartDto(cart));
    }
}
