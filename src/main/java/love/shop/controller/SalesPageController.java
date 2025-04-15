package love.shop.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.category.Category;
import love.shop.domain.salesPage.SalesPage;
import love.shop.repository.ItemPage.SalesPageRepository;
import love.shop.repository.item.ItemRepository;
import love.shop.service.item.ItemService;
import love.shop.service.itemPage.SalesPageService;
import love.shop.web.item.searchCond.SearchCond;
import love.shop.web.item.searchFilter.SearchFilter;
import love.shop.web.itemPage.dto.CreatePageReqDto;
import love.shop.web.itemPage.dto.ItemSalesPageDto;
import love.shop.web.itemPage.dto.ModifyPageReqDto;
import love.shop.web.page.dto.SalesPageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SalesPageController {

//    이후에 페이지에 해당하는 리뷰 만들기

    private final SalesPageService itemPageService;
    private final ItemRepository itemRepository;
    private final SalesPageRepository itemPageRepository;
    private final ItemService itemService;
    private final SalesPageService salesPageService;

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
        Category category = itemService.findCategoryById(searchCond.getCategories());
        String type = category.getType(); // 해당 카테고리의 데이터 타입 확인

        // 클라이언트 쪽에서 보여줄 필터
        SearchFilter filters = SearchFilter.findFilter(type);

        List<SalesPage> salesPageList = salesPageService.findSalesPageByItemCategoryAndSearchCond(searchCond, convertedFilter, offset, limit);
        log.info("조건으로 찾은 SalesPage={}", salesPageList);
        // 검색 조건으로 찾은 아이템 Dto로 변환
//        List<ItemDto> itemDtoList = ItemDto.createItemDtoList(items);

        // dto 변환
        List<SalesPageDto> pageDtoList = SalesPageDto.createPageDtoList(salesPageList);

        // 응답 데이터 형태로 감싸기
        ItemController.ItemListPageResult<Object> objectItemListPageResult = new ItemController.ItemListPageResult<>(pageDtoList, filters, type);

        return ResponseEntity.ok(objectItemListPageResult);
    }

    @PatchMapping("/page/{pageId}")
    public ResponseEntity<?> modifyItemPage(@PathVariable Long pageId, @RequestBody ModifyPageReqDto modifyPageReqDto) {

        // 기존에 등록 되어 있는 아이템을 다시 등록할려고하면 예외 처리 해야함.
        // 이 컨트롤러는 예외 처리가 전혀 되어 있지 않음
        // 1. 등록되어 있지 않은 itemPage를 삭제하려 할때,
        // 2. 이미 등록 되어 있는 아이템을 등록하려 할때

        SalesPage page = itemPageService.modifyItemPage(modifyPageReqDto.getPageId(), modifyPageReqDto.getDeleteItems(),
                modifyPageReqDto.getPageName(), modifyPageReqDto.getImages(), modifyPageReqDto.getDescription(),
                modifyPageReqDto.getOptionAndItem());// 이렇게 dto를 넘기는건 다른 곳에선 사용 못한다. 이걸 좀 풀어줘야할듯

        SalesPageDto pageDto = new SalesPageDto(page);

        return ResponseEntity.ok(pageDto);
    }

    @PostMapping("/page")
    public ResponseEntity<?> createItemPage(@RequestBody CreatePageReqDto pageReqDto) {
        log.info("아이템 판매 페이지 생성={}", pageReqDto);

        // 판매 페이지를 생성할 때 계층형 아이템 옵션을 만들어 줘야함.
        //CreatePageReqDto를 그에 알맞게 변형 시켜야함. 밑에 createItemPage


        SalesPage page = itemPageService.createItemPage(pageReqDto);
        SalesPageDto pageDto = new SalesPageDto(page);

        return ResponseEntity.ok(pageDto);
    }

    // 해당 카테고리를 가진 아이템을 가진 페이지 조회
    @GetMapping("/pageList/")
    public ResponseEntity<?> findItemPage(@RequestParam Long categoryId,
                                          @RequestParam(value = "offset", defaultValue = "0") int offset,
                                          @RequestParam(value = "limit", defaultValue = "100") int limit) {
        log.info("페이지 아이템 카테고리 id로 조회하기 시작={}", categoryId);

        List<SalesPageDto> pageDtoList = itemPageService.findItemPageWithItemCategory(categoryId, offset, limit);

        // 아이템 몇개 채워넣고 카테고리 및 필터링 검색으로 해당 아이템을 가진 판매 페이지 조회로 가보자

        return ResponseEntity.ok(pageDtoList);
    }

    // 단건 조회인데 왜 페이징이 들어가 있지? 아 리뷰 페이징
    // 판매 페이지 조회
    @GetMapping("/salesPage/{pageId}")
    public ResponseEntity<?> findPageByPageId(@PathVariable Long pageId,
                                              @RequestParam(name = "reviewOffset", defaultValue = "0") int reviewOffset,
                                              @RequestParam(name = "reviewLimit", defaultValue = "10") int reviewLimit) {
        log.info("페이지 id로 페이지 찾기={}", pageId);

        // 페이지 조회
        SalesPageDto pageDto = itemPageService.findPageByPageId(pageId);

        // 리뷰는 따로 떠와서 wrapper에 넣어준다.
        // 방안 1. 페이지와 리뷰를 따로 데이터베이스에 접근해서 가져온다. 리뷰는 페이징으로 가져온다.
        // 방안 2. 페이지 가져올때

        // 페이징 구현하는거 자세히 알기

        // 리뷰, 각 판매 페이지에서 페이징으로 가져오는거 구현하기,
        // 판매 페이지 레이아웃 간단히 만들고, 옵션 구현된거 제대로 꾸며보기

        ItemSalesPageDto mainItem = null;
        List<ItemSalesPageDto> itemPages = pageDto.getItemSalesPages();
        for (ItemSalesPageDto itemPage : itemPages) {
            if (itemPage.getIsMainItem()) {
                mainItem = itemPage;
            }
        }


        PageResultWrapper<Object> result = new PageResultWrapper<>(pageDto, mainItem,"ok");
        return ResponseEntity.ok(result);
    }

    @Data
    @AllArgsConstructor
    static class PageResultWrapper<T> {
        private T page;
        private T mainItem;
        private T review;
    }

    @DeleteMapping("/page/{pageId}")
    public ResponseEntity<?> deletePage(@PathVariable Long pageId) {
        itemPageService.deletePage(pageId);

        /*
        예외 처리
        1. 이미 비활성화 되어 있는 페이지일 경우
        2. 없는 페이지일 경우
         */

        return ResponseEntity.ok("삭제 완료. 사실은 페이지 비활성화");
    }
}
