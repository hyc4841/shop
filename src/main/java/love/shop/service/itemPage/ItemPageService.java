package love.shop.service.itemPage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.item.Item;
import love.shop.domain.itemPage.ItemPage;
import love.shop.domain.page.Page;
import love.shop.repository.ItemPage.ItemPageRepository;
import love.shop.repository.item.ItemRepository;
import love.shop.web.itemPage.dto.CreatePageReqDto;
import love.shop.web.itemPage.dto.ModifyPageReqDto;
import love.shop.web.page.dto.PageDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemPageService {

    private final ItemPageRepository itemPageRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Page modifyItemPage(Long pageId, List<Long> deleteItems, String pageName,
                               List<String> images, String description, Map<String, List<Long>> optionAndItem) {

        Page page = itemPageRepository.findPageByPageId(pageId).orElseThrow(() -> new RuntimeException());

        // 1. 기존에 등록되어 있는 아이템 삭제할거 있으면 삭제함
        if (deleteItems != null) {
            for (Long itemPageId : deleteItems) {
                ItemPage itemPage = itemPageRepository.findItemPageByItemPageId(itemPageId).orElseThrow(() -> new RuntimeException());
                itemPage.deleteItemPage();
                itemPageRepository.deleteItemPage(itemPageId);
            }
        }

        // 2. 판매 페이지 타이틀 변경사항 있으면 바꿔준다
        page.modifyPage(pageName, description, images);

        // 3. 새로 등록할 아이템 등록하기
        itemPageReg(page, optionAndItem);

        itemPageRepository.savePage(page);

        return page;
    }

    // 아이템 페이지 만들기(판매 페이지)
    @Transactional
    public Page createItemPage(CreatePageReqDto pageReqDto) {
        Page page = new Page(pageReqDto.getPageName(), pageReqDto.getImages(), pageReqDto.getDescription());

        itemPageReg(page, pageReqDto.getOptionAndItem());

        itemPageRepository.savePage(page);

        return page;
    }

    // 아이템의 카테고리로 아이템 페이지 찾기
    public List<PageDto> findItemPageWithItemCategory(Long categoryId, int offset, int limit) {

        List<Page> pageByItemCategory = itemPageRepository.findPageByItemCategory(categoryId, offset, limit);

        return PageDto.createPageDtoList(pageByItemCategory);
    }

    // 페이지 단건 조회
    public PageDto findPageByPageId(Long pageId) {
        Page page = itemPageRepository.findPageByPageId(pageId).orElseThrow(() -> new RuntimeException());
        return new PageDto(page);
    }

    private void itemPageReg(Page page, Map<String, List<Long>> optionAndItem) {
        optionAndItem.forEach((key, value) -> {
            for (Long itemId : value) {
                log.info("반복");
                ItemPage itemPage = new ItemPage(key);
                Item item = itemRepository.findOne(itemId).orElseThrow(() -> new RuntimeException());
                itemPage.setItem(item);
                itemPage.setPage(page);
            }
        });
    }

    // 실제론
    @Transactional
    public void deletePage(Long pageId) {
        Page page = itemPageRepository.findPageByPageId(pageId).orElseThrow(() -> new RuntimeException());
        page.deletePage(); // 페이지 활성화만 false로 바꾸는걸로 하자

    }

}

