package love.shop.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.salesPage.SalesPage;
import love.shop.repository.ItemPage.SalesPageRepository;
import love.shop.repository.item.ItemRepository;
import love.shop.service.item.ItemService;
import love.shop.service.itemPage.SalesPageService;
import love.shop.web.itemPage.dto.CreatePageReqDto;
import love.shop.web.itemPage.dto.ModifyPageReqDto;
import love.shop.web.page.dto.SalesPageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SalesPageController {

//    이후에 페이지에 해당하는 리뷰 만들기

    private final SalesPageService itemPageService;
    private final ItemRepository itemRepository;
    private final SalesPageRepository itemPageRepository;
    private final ItemService itemService;

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

        return ResponseEntity.ok(pageDtoList);
    }

    // 단건 조회인데 왜 페이징이 들어가 있지? 아 리뷰 페이징
    // 판매 페이지 조회
    @GetMapping("/page/{pageId}")
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

        PageResultWrapper<Object> result = new PageResultWrapper<>(pageDto, "ok");
        return ResponseEntity.ok(result);
    }

    @Data
    @AllArgsConstructor
    static class PageResultWrapper<T> {
        private T page;
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
