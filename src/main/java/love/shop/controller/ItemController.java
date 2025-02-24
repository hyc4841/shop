package love.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.category.Category;
import love.shop.domain.item.Book;
import love.shop.service.item.ItemService;
import love.shop.web.item.dto.BookSaveDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/item")
    public ResponseEntity<String> saveItem(@RequestBody BookSaveDto bookDto) {
        // 그럼 책은 책대로, 전자제품은 전자제품대로 각자 다른 정보를 가지고 있어야 한다?
        // 아이템을 저장할 때 필요한 것은 아이템 그자체와,

        for (String categoryName : bookDto.getCategories()) {

        }

        Book book = new Book(bookDto.getAuthor(), bookDto.getIsbn(), bookDto.getName(), bookDto.getPrice(), bookDto.getStockQuantity());


        return ResponseEntity.ok("ok");
    }

}
