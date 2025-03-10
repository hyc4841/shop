package love.shop.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.ItemCategory.ItemCategory;
import love.shop.domain.category.Category;
import love.shop.domain.item.type.Book;
import love.shop.domain.item.Item;
import love.shop.repository.item.ItemRepository;
import love.shop.web.item.updateDto.BookUpdateReqDto;
import love.shop.web.item.saveDto.ItemSaveReqDto;
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

    // 아이템 단일 저장
    @Transactional
    public void save(Item item) {
        itemRepository.save(item);

        // 보통 아이템을 저장할 땐 카테고리를 같이 넣어서 저장한다.
    }

    @Transactional
    public Item saveItemWithCategory(ItemSaveReqDto itemDto) {

        Item item = Item.createItem(itemDto);// 아이템 저장 요청으로 들어온 DTO를 각 데이터타입에 맞게 구분한 후 해당 데이터타입의

        if (item != null) {
            // 아이템에 카테고리 등록
            for (Integer categoryId : itemDto.getCategoriesId()) {
                Category category = itemRepository.findCategoryById(Long.valueOf(categoryId)).orElseThrow(() -> new RuntimeException("해당 카테고리가 없음"));
                ItemCategory itemCategory = ItemCategory.createItemCategory(category);
                item.addItemCategory(itemCategory);
            }
        }
        save(item);

        return item;
    }

    // 아이템 id로 조회
    public Item findOne(Long ItemId) {
        return itemRepository.findOne(ItemId).orElseThrow(() -> new RuntimeException("값이 없음"));
    }

    // 모든 아이템 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    // 카테고리명으로 카테고리 조회
    public Category findCategoryByName(String categoryName) {
        return itemRepository.findCategoryByName(categoryName).orElseThrow(() -> new RuntimeException("값이 없음"));
    }

    // 카테고리 이름으로 조회
    public List<Category> findCategoryListByName(String categoryName) {
        return itemRepository.findCategoryListByName(categoryName);
    }

    // 카테고리 이름과 부모 카테고리 이름으로 조회
    public Category findCategoryByNameAndParentName(String categoryName, String parentName) {
        return itemRepository.findCategoryByNameAndParentName(categoryName, parentName).orElseThrow(() -> new RuntimeException("값이 없음"));
    }

    public List<Category> findAllCategory() {
        return itemRepository.findAllCategory();
    }

    // 아이템 조건 검색
    public List<Item> findItemsBySearchCond(SearchCond searchCond, int offset, int limit) {
        return itemRepository.findItemsBySearchCond(searchCond, offset, limit);
    }

    // 책 수정
    public Book updateBook(BookUpdateReqDto bookDto) {
        itemRepository.updateBook(bookDto);
        Item item = itemRepository.findOne(bookDto.getItemId()).orElseThrow(() -> new RuntimeException("값이 없음"));
        return (Book) item;
    }

    // 아이템 삭제
    @Transactional
    public void deleteItem(Long itemId) {
        itemRepository.deleteItem(itemId);
    }

}
