package love.shop.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.category.Category;
import love.shop.domain.item.type.Book;
import love.shop.domain.item.Item;
import love.shop.repository.item.ItemRepository;
import love.shop.service.item.ItemService;
import love.shop.web.item.dto.*;
import love.shop.web.item.saveDto.ItemSaveReqDto;
import love.shop.web.item.searchCond.SearchCond;
import love.shop.web.item.updateDto.BookUpdateReqDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @PostMapping("/item")
    public ResponseEntity<ItemDto> saveItemWithCategory(@RequestBody @Validated ItemSaveReqDto itemDto) {

        log.info("itemDto={}", itemDto);
        log.info("아이템 카테고리별로 저장하기");

        Item savedItem = itemService.saveItemWithCategory(itemDto);

        ItemDto savedItemDto = ItemDto.createItemDto(savedItem);


        return ResponseEntity.ok(savedItemDto);
    }

    // 카테고리별 아이템 리스트를 건네주는 컨트롤러가 따로 있어야할듯.
    // 지금 이 메서드를 카테고리별 아이템 조회로 만들어야할듯

    // 카테고리 중분류 아이템 조건 검색
    @GetMapping("/items")
    public ResponseEntity<List<ItemDto>> items(@ModelAttribute SearchCond searchCond, // 중분류 카테고리로 뿌려주는걸로 만들자
                                               @RequestParam(value = "offset", defaultValue = "0") int offset,
                                               @RequestParam(value = "limit", defaultValue = "50") int limit) {
        log.info("searchCond={}", searchCond);

        List<Item> items = itemService.findItemsBySearchCond(searchCond, offset, limit);
        log.info("조건으로 찾은 items={}", items);

        for (Long categoryId : searchCond.getCategories()) {
            Category category = itemService.findCategoryById(categoryId);

        }

        List<ItemDto> itemDtoList = ItemDto.createItemDtoList(items);

        return ResponseEntity.ok(itemDtoList);
    }

    // 아이템 상세 조회
    @GetMapping("/item/{itemId}")
    public ResponseEntity<String> findItemById(@PathVariable Long itemId) {
        Item item = itemService.findOne(itemId);

        // 아이템 조회부터 다시 만들자

        return ResponseEntity.ok("ok");
    }
    //

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


//    @GetMapping("/items")
    public ResponseEntity<List<ItemDto>> findItemsByCategories(@RequestParam("categoryId") Long categoryId) {
        log.info("categoryId={}", categoryId);
        // 아이템 조회
        Category findCategory = itemService.findCategoryById(categoryId);
        List<Item> items = itemService.findItemsByCategoryId(categoryId);
        List<ItemDto> itemDtoList = ItemDto.createItemDtoList(items);
        return ResponseEntity.ok(itemDtoList);
    }


    @Data
    @AllArgsConstructor
    static class ItemListResult<T> {
        private T itemList;
        private T filterList;

    }


    // 카테고리 테스트

}
