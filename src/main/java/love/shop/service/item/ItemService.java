package love.shop.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.category.Category;
import love.shop.domain.item.Book;
import love.shop.domain.item.Item;
import love.shop.repository.item.ItemRepository;
import love.shop.web.item.dto.BookUpdateReqDto;
import love.shop.web.item.dto.SearchCond;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    // 아이템 저장
    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }

    // 아이템 id로 조회
    public Item findOne(Long ItemId) {
        return itemRepository.findOne(ItemId);
    }

    // 모든 아이템 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    // 카테고리명으로 카테고리 조회
    public Category findCategoryByName(String categoryName) {
        return itemRepository.findCategoryByName(categoryName);
    }

    public Category findCategoryByNameAndParentName(String categoryName, String parentName) {
        return itemRepository.findCategoryByNameAndParentName(categoryName, parentName);
    }

    // 아이템 조건 검색
    public List<Item> findItemsBySearchCond(SearchCond searchCond, int offset, int limit) {
        return itemRepository.findItemsBySearchCond(searchCond, offset, limit);
    }

    // 책 수정
    public Book updateBook(BookUpdateReqDto bookDto) {
        itemRepository.updateBook(bookDto);
        Item item = itemRepository.findOne(bookDto.getItemId());
        return (Book) item;
    }

    // 아이템 삭제
    @Transactional
    public void deleteItem(Long itemId) {
        itemRepository.deleteItem(itemId);
    }
}
