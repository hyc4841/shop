package love.shop.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.category.Category;
import love.shop.domain.item.type.Book;
import love.shop.domain.item.Item;
import love.shop.domain.itemSpec.ItemSpec;
import love.shop.repository.item.ItemRepository;
import love.shop.service.item.ItemService;
import love.shop.web.category.dto.CategoryDto;
import love.shop.web.item.dto.*;
import love.shop.web.item.saveDto.ItemSaveReqDto;
import love.shop.web.item.searchCond.SearchCond;
import love.shop.web.item.searchFilter.SearchFilter;
import love.shop.web.item.updateDto.BookUpdateReqDto;
import love.shop.web.itemSpec.dto.ItemSpecDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    /***
     * test
     * 모든 상품 스펙 데이터 타입별로 조회.
     */
    @GetMapping("/test/item-spec")
    public ResponseEntity<?> findAllItemSpec() {

        List<ItemSpec> allItemSpec = itemService.findAllItemSpec();

        Map<String, List<ItemSpecDto>> itemSpecDtoMap = new HashMap<>();

        for (ItemSpec itemSpec : allItemSpec) {
            ItemSpecDto filter = SearchFilter.findFilter(itemSpec.getDataType(), itemSpec);
            if (itemSpecDtoMap.get(itemSpec.getDataType()) == null) {
                List<ItemSpecDto> itemSpecDtos = new ArrayList<>();
                itemSpecDtos.add(filter);
                itemSpecDtoMap.put(itemSpec.getDataType(), itemSpecDtos);
            } else {
                itemSpecDtoMap.get(itemSpec.getDataType()).add(filter);
            }
        }

        return ResponseEntity.ok(itemSpecDtoMap);
    }



    @GetMapping("/item")
    public ResponseEntity<?> saveItemPage() {
        // 아이템 추가 페이지에 넘겨줘야할 데이터
        // 모든 카테고리(계층으로 정리해서) - 성공.
        // 모든 상품 스펙(상품 타입에 맞게 정리해서) - 성공

        // 모든 상품 스펙.
        List<ItemSpec> allItemSpec = itemService.findAllItemSpec();
        Map<String, List<ItemSpecDto>> itemSpecDtoMap = new HashMap<>(); // <데이터 타입, 상품 스펙> 상품 스펙 리스트
        List<String> itemDataType = new ArrayList<>(); // 상품 데이터 타입

        for (ItemSpec itemSpec : allItemSpec) {
            ItemSpecDto filter = SearchFilter.findFilter(itemSpec.getDataType(), itemSpec);
            if (itemSpecDtoMap.get(itemSpec.getDataType()) == null) {
                List<ItemSpecDto> itemSpecDtos = new ArrayList<>();
                itemSpecDtos.add(filter);
                itemSpecDtoMap.put(itemSpec.getDataType(), itemSpecDtos);
                itemDataType.add(itemSpec.getDataType());
            } else {
                itemSpecDtoMap.get(itemSpec.getDataType()).add(filter);
            }
        }

        List<CategoryDto> depthCategory = itemService.getDepthCategory();

        ItemSavePageResult<Object> result = new ItemSavePageResult<>(itemSpecDtoMap, depthCategory, itemDataType);

        return ResponseEntity.ok(result);
    }

    @Data
    @AllArgsConstructor
    static class ItemSavePageResult<T> {
        private T allItemSpec;
        private T allCategories;
        private T itemDataType;
    }


    // 아이템 저장.
    @PostMapping("/item")
    public ResponseEntity<?> saveItemWithCategory(@RequestBody @Validated ItemSaveReqDto itemDto) {
        log.info("받은 데이터={}", itemDto);

        Item savedItem = itemService.saveItemWithCategory(itemDto);
        ItemDto savedItemDto = ItemDto.createItemDto(savedItem);

        return ResponseEntity.ok(savedItemDto);
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

    // 대분류 카테고리 조회. 모든 카테고리 끌고와서 계층 만들어 주는
    @GetMapping("/category/major")
    public ResponseEntity<?> findMajorCategory() {
        log.info("대분류 카테고리 조회");
        List<CategoryDto> depthCategory = itemService.getDepthCategory();

        return ResponseEntity.ok(depthCategory);
    }

    private CategoryDto toCategoryDto(List<Category> categories, Category parent) {
        List<CategoryDto> childrenDtoList = new ArrayList<>();

        CategoryDto categoryDto = new CategoryDto(parent);
        // 여기서 넘어온 parent

        List<Category> children = categories.stream().filter(category -> ObjectUtils.isNotEmpty(category.getParent()) &&
                                                                        category.getParent().getId().equals(parent.getId()))
                .collect(Collectors.toList());

        for (Category child : children) {
            CategoryDto childCategoryDto = toCategoryDto(categories, child);
            childrenDtoList.add(childCategoryDto);
        }

        categoryDto.setChildren(childrenDtoList);

        return categoryDto;
    }

    @Data
    @AllArgsConstructor
    static class ItemListResult<T> {
        private T itemList;
        private T filterList;
    }


    // 아이템하고 카테고리 분리할까...



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
