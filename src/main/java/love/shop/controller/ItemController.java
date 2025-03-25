package love.shop.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.cart.Cart;
import love.shop.domain.category.Category;
import love.shop.domain.item.type.Book;
import love.shop.domain.item.Item;
import love.shop.domain.member.Member;
import love.shop.repository.item.ItemRepository;
import love.shop.service.item.ItemService;
import love.shop.service.member.MemberService;
import love.shop.web.cart.dto.CartSaveReqDto;
import love.shop.web.category.dto.CategoryDto;
import love.shop.web.item.dto.*;
import love.shop.web.item.saveDto.ItemSaveReqDto;
import love.shop.web.item.searchCond.SearchCond;
import love.shop.web.item.searchFilter.LapTopSearchFilter;
import love.shop.web.item.searchFilter.SearchFilter;
import love.shop.web.item.searchFilter.TVSearchFilter;
import love.shop.web.item.updateDto.BookUpdateReqDto;
import love.shop.web.login.dto.CustomUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import love.shop.web.cart.dto.CartDto;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final MemberService memberService;

    // 아이템 저장.
    @PostMapping("/item")
    public ResponseEntity<ItemDto> saveItemWithCategory(@RequestBody @Validated ItemSaveReqDto itemDto) {

        log.info("itemDto={}", itemDto);
        log.info("아이템 카테고리별로 저장하기");

        Item savedItem = itemService.saveItemWithCategory(itemDto);

        ItemDto savedItemDto = ItemDto.createItemDto(savedItem);

        return ResponseEntity.ok(savedItemDto);
    }

    // 카테고리 중분류 아이템 조건 검색
    // 아이템 조회
    @GetMapping("/items")
    public ResponseEntity<?> items(@ModelAttribute SearchCond searchCond, // 중분류 카테고리로 뿌려주는걸로 만들자
                                   @RequestParam Map<String, String> checkedFilter,
                                   @RequestParam(value = "offset", defaultValue = "0") int offset,
                                   @RequestParam(value = "limit", defaultValue = "50") int limit) {

        checkedFilter.remove("itemName");
        checkedFilter.remove("morePrice");
        checkedFilter.remove("categories");
        checkedFilter.remove("type");
        checkedFilter.remove("lessPrice");

        // 1. 스트링으로 들어온 Map<String, String> filter 를 Map<String, List<String>> 으로 변환하기
        Map<String, List<String>> convertedFilter = new HashMap<>();

        for (String key : checkedFilter.keySet()) {
            List<String> values = List.of(checkedFilter.get(key).split(","));
            convertedFilter.put(key, values);
        }

        log.info("들어온 조건들={}", checkedFilter);
        log.info("변환한 조건들={}", convertedFilter);
        log.info("searchCond={}", searchCond);

        // 타입이 없고 카테고리 id만 넘어온다면
        Category findCategory = itemService.findCategoryType(searchCond.getCategories());
        String type = findCategory.getType();

        // 클라이언트 쪽에서 보여줄 필터
        SearchFilter filters = SearchFilter.findFilter(type);

        List<Item> items = itemService.findItemsBySearchCond(searchCond, convertedFilter, offset, limit);
        log.info("조건으로 찾은 items={}", items);
        // 검색 조건으로 찾은 아이템 Dto로 변환
        List<ItemDto> itemDtoList = ItemDto.createItemDtoList(items);

        // 응답 데이터 형태로 감싸기
        ItemListPageResult<Object> objectItemListPageResult = new ItemListPageResult<>(itemDtoList, filters, type);

        return ResponseEntity.ok(objectItemListPageResult);
    }

    @Data
    @AllArgsConstructor
    static class ItemListPageResult<T> {
        private T itemList;
        private T itemFilter; // 클라이언트 화면에 보일 필터 부분
        private String type;
    }

    // 아이템 상세 조회. 아이템 페이지에 뿌려줄 데이터
    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> findItemById(@PathVariable Long itemId) {
        Item item = itemService.findOne(itemId);

        log.info("아이템 단건 조회={}", item);

        ItemDto itemDto = ItemDto.createItemDto(item);

        return ResponseEntity.ok(itemDto);
    }

    @PatchMapping("/item/book")
    public ResponseEntity<BookDto> updateBook(@RequestBody BookUpdateReqDto bookDto) {
        Book updatedBook = itemService.updateBook(bookDto);

        BookDto updatedBookDto = new BookDto(updatedBook);
        return ResponseEntity.ok(updatedBookDto);
    }

    // 삭제
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<String> deleteItem(@RequestParam Long itemId) {
        itemService.deleteItem(itemId);

        return ResponseEntity.ok("삭제 완료");
    }

    // 모든 카테고리 조회
    @GetMapping("/category")
    public ResponseEntity<List<CategoryDto>> findAllCategory() {
        log.info("모든 카테고리 조회");
        List<Category> allCategory = itemService.findAllCategory();

        List<CategoryDto> categoryDtos = allCategory.stream().map(category -> new CategoryDto(category))
                .collect(Collectors.toList());

        return ResponseEntity.ok(categoryDtos);
    }

    // 대분류 카테고리 조회
    @GetMapping("/category/major")
    public ResponseEntity<List<CategoryDto>> findMajorCategory() {
        log.info("대분류 카테고리 조회");

        List<Category> majorCategory = itemRepository.findMajorCategory();

        List<CategoryDto> categoryDtoList = majorCategory.stream().map(category -> new CategoryDto(category))
                .collect(Collectors.toList());

        return ResponseEntity.ok(categoryDtoList);
    }

    @Data
    @AllArgsConstructor
    static class ItemListResult<T> {
        private T itemList;
        private T filterList;
    }

    // 장바구니 생성

    @PostMapping("/cart")
    public ResponseEntity<?> createCart() {

        return ResponseEntity.ok("ok");
    }

    // 장바구니 저장? 이건 수정이 필요함. 장바구니에 상품 추가?
    @PatchMapping("/cart")
    public ResponseEntity<?> addCart(@RequestBody CartSaveReqDto cartSaveReqDto) {

        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기
        Member member = memberService.findMemberById(memberId);

        itemService.saveCart(cartSaveReqDto, member);

        return ResponseEntity.ok("ok");
    }

    // 장바구니에 상품 제거
    @DeleteMapping("/cart")
    public ResponseEntity<?> removeCartItem(@RequestParam Long itemCartId) {
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기

        itemService.removeCartItem(itemCartId, memberId);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/cart")
    public ResponseEntity<?> findMemberCart() {
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId(); // jwt 토큰으로 부터 멤버 정보 가져오기

        Cart cart = itemService.findCartByMemberId(memberId);
        CartDto cartDto = new CartDto(cart);

        return ResponseEntity.ok(cartDto);
    }


    // 카테고리 테스트
    /*

    @GetMapping("/test123")
    public ResponseEntity<?> test123(@RequestParam Map<String, String> test) {

        List<String> tests = List.of(test.get("test").split(","));

        for (String s : test.keySet()) {
            log.info(s);
        }

        log.info("나눈거={}", tests);

        log.info("test={}", test);

        return ResponseEntity.ok(test);
    }

     */

}
