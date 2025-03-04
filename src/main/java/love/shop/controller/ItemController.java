package love.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.item.Book;
import love.shop.domain.item.Item;
import love.shop.service.item.ItemService;
import love.shop.web.item.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/item")
    public ResponseEntity<String> saveItem(@RequestBody BookSaveReqDto bookDto) {
        // 그럼 책은 책대로, 전자제품은 전자제품대로 각자 다른 정보를 가지고 있어야 한다?
        // 아이템을 저장할 때 필요한 것은 아이템 그자체와


        // 일대다, 다대일, 객체 생성하고 언제 어디서 넣어줘야하는지, 특히 Many 애들 어떻게 해줘야하는지
        // 지금은 뭘하고 있냐면 아이템 생성하고 저장하려고 하는데 아이템-카테고리 아이템 생성할 때 같이 저장하고 싶은데 어떻게 할지
        // 헤매고 있는중임.
        // 상속 관계일 때는 객체 어떻게 생성하는지도 확실히 알아야함.

        return ResponseEntity.ok("ok");
    }

    // 아이템 조건 검색
    @GetMapping("/items")
    public ResponseEntity<List<ItemDto>> items(@RequestBody SearchCond searchCond,
                                               @RequestParam(value = "offset", defaultValue = "0") int offset,
                                               @RequestParam(value = "limit", defaultValue = "50") int limit) {
        log.info("searchCond={}", searchCond);

        List<Item> items = itemService.findItemsBySearchCond(searchCond, offset, limit);
        log.info("조건으로 찾은 items={}", items);

        List<ItemDto> result = new ArrayList<>();

        for (Item item : items) {
            switch (item.getType()) {
                case "Book":
                    ItemDto bookDto = new BookDto((Book) item);
                    result.add(bookDto);
                    break;
            }
        }

        return ResponseEntity.ok(result);
    }

    // 아이템 상세 조회
    @GetMapping("/item/{itemId}")
    public ResponseEntity findItemById(@PathVariable Long itemId) {
        Item item = itemService.findOne(itemId);

        String type = item.getType();
        // 아이템 타입에 따라서 아이템 dto를 내줘야한다?



        new ItemDto()
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
}
