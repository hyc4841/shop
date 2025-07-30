package love.shop.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.CategoryNotExistException;
import love.shop.domain.ItemCategory.ItemCategory;
import love.shop.domain.category.Category;
import love.shop.domain.item.type.Book;
import love.shop.domain.item.Item;
import love.shop.domain.itemSpec.ItemSpec;
import love.shop.repository.item.ItemRepository;
import love.shop.web.category.dto.CategoryDto;
import love.shop.web.item.searchFilter.SearchFilter;
import love.shop.web.item.updateDto.BookUpdateReqDto;
import love.shop.web.item.saveDto.ItemSaveReqDto;
import love.shop.web.item.searchCond.SearchCond;
import love.shop.web.itemSpec.dto.ItemSpecDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * Test
     * 모든 상품 스펙 조회
     */
    public List<ItemSpec> findAllItemSpec() {
        return itemRepository.findAllItemSpec();
    }


    // 아이템 단일 저장
    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }


    @Transactional
    public void saveItemSpecList(ItemSpec itemSpec) {
        itemRepository.saveItemSpec(itemSpec);
    }

    @Cacheable(value = "specListCache", key = "#dtype")
    public ItemSpecDto findItemSpec(String dtype) {
        log.info("아이템 스펙 찾는중={}", dtype);
        ItemSpec itemSpec = itemRepository.findItemSpec(dtype).orElseThrow(() -> new RuntimeException("해당 아이템 스펙이 없습니다."));
        ItemSpecDto filter = SearchFilter.findFilter(dtype, itemSpec);

        return filter;
    }

    // 관리자 쪽에서 스펙 바꼇다고 캐시 갱신하는 로직임. 서버를 끄지 않고 운영하는 도중에 사용한다. 그럼 @Cacheable 어노테이션 안붙은거 실시간으로 db에서
    // 떼오는 로직 하나 만들어 두고, 스펙 변경 생기면 거기에서 갱신된 데이터 받아서 이 로직 호출하면 실시간으로 캐시 데이터가 바뀐다는 소리
    @CacheEvict(value = "specListCache", key = "#dtype")
    public void updateTvSpecCache(String dtype, ItemSpecDto newSpec) {

    }

    @Cacheable(value = "categoryCache")
    public List<CategoryDto> getDepthCategory() {

        List<Category> categories = itemRepository.findAllCategory();

        List<Category> parents = categories.stream().filter(category -> ObjectUtils.isEmpty(category.getParent()))
                .collect(Collectors.toList());

        List<CategoryDto> categoryDtoList = new ArrayList<>();

        // 카테고리 계층 형성 로직
        for (Category parent : parents) {
            CategoryDto categoryDto = toCategoryDto(categories, parent); // 플렛하게 가져온 카테고리를 계층으로 만들어주는 함수
            categoryDtoList.add(categoryDto);
        }

        return categoryDtoList;
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

    @Transactional
    public Item saveItemWithCategory(ItemSaveReqDto saveReqDto) {

        Item item = Item.createItem(saveReqDto); // 아이템 저장 요청으로 들어온 DTO를 각 데이터타입에 맞게 구분한 후 해당 데이터타입의

        if (item != null) {
            // 아이템에 카테고리 등록
            for (Integer categoryId : saveReqDto.getCategoriesId()) {
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

    public List<Item> findItemsByItemId(List<Long> items) {
        return itemRepository.findItemsByItemId(items);
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

    public Category findCategoryById(Long categoryId) {
        return itemRepository.findCategoryById(categoryId).orElseThrow(() -> new CategoryNotExistException("해당 카테고리는 존재하지 않습니다."));
    }

    /*
    // 아이템 조건 검색
    public List<Item> findItemsBySearchCond(SearchCond searchCond, Map<String, List<String>> filters, int offset, int limit) {
        return itemRepository.findItemsBySearchCond(searchCond, filters, offset, limit);
    }
     */

    public List<Item> findItemsByCategories(List<String> categories) {
        return itemRepository.findItemsByCategories(categories);
    }

    public List<Item> findItemsByCategoryId(Long categoryId) {
        return itemRepository.findItemsByCategoryId(categoryId);
    }

    // 책 수정A
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
