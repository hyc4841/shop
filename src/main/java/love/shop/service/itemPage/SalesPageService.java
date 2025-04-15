package love.shop.service.itemPage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.item.Item;
import love.shop.domain.itemSalesPage.ItemSalesPage;
import love.shop.domain.salesPage.SalesPage;
import love.shop.repository.ItemPage.SalesPageRepository;
import love.shop.repository.item.ItemRepository;
import love.shop.web.item.searchCond.SearchCond;
import love.shop.web.itemPage.dto.CreatePageReqDto;
import love.shop.web.page.dto.SalesPageDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SalesPageService {

    private final SalesPageRepository salesPageRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public SalesPage modifyItemPage(Long pageId, List<Long> deleteItems, String pageName,
                                    List<String> images, String description, Map<String, Map<Long, Map<String, Boolean>>> optionAndItem) {

        SalesPage page = salesPageRepository.findPageByPageId(pageId).orElseThrow(() -> new RuntimeException());

        // 1. 기존에 등록되어 있는 아이템 삭제할거 있으면 삭제함
        if (deleteItems != null) {
            for (Long itemPageId : deleteItems) {
                ItemSalesPage itemPage = salesPageRepository.findItemPageByItemPageId(itemPageId).orElseThrow(() -> new RuntimeException());
                itemPage.deleteItemPage();
                salesPageRepository.deleteItemPage(itemPageId);
            }
        }

        // 2. 판매 페이지 타이틀 변경사항 있으면 바꿔준다
        page.modifyPage(pageName, description, images);

        // 3. 새로 등록할 아이템 등록하기
        itemPageReg(page, optionAndItem);

        salesPageRepository.savePage(page);

        return page;
    }

    // 아이템 페이지 만들기(판매 페이지)
    @Transactional
    public SalesPage createItemPage(CreatePageReqDto pageReqDto) {
        SalesPage page = new SalesPage(pageReqDto.getPageName(), pageReqDto.getImages(), pageReqDto.getDescription());

        itemPageReg(page, pageReqDto.getOptionAndItem());

        salesPageRepository.savePage(page);

        return page;
    }

    // 아이템의 카테고리로 아이템 페이지 찾기
    public List<SalesPageDto> findItemPageWithItemCategory(Long categoryId, int offset, int limit) {

        List<SalesPage> pageByItemCategory = salesPageRepository.findPageByItemCategory(categoryId, offset, limit);

        return SalesPageDto.createPageDtoList(pageByItemCategory);
    }

    // 페이지 단건 조회
    public SalesPageDto findPageByPageId(Long pageId) {
        SalesPage page = salesPageRepository.findPageByPageId(pageId).orElseThrow(() -> new RuntimeException());
        return new SalesPageDto(page);
    }

    private void itemPageReg(SalesPage page, Map<String, Map<Long, Map<String, Boolean>>> optionAndItem) {
        /*
        "옵션 이름": {
            {
                "아이템id": {
                    "상품표시이름": 메인상품 표시
                },
            },
        },
        */
        optionAndItem.forEach((optionName, itemObject) -> {
            // itemObject -> <itemId, <itemDisplayName, mainItemFlag>
            itemObject.forEach((itemId, itemDisplayNameAndMainFlag) -> {
                ItemSalesPage itemSalesPage = new ItemSalesPage(optionName);

                itemDisplayNameAndMainFlag.forEach((itemDisplayName, mainFlag) -> {
                    Item item = itemRepository.findOne(itemId).orElseThrow(() -> new RuntimeException("아이템이 존재하지 않습니다"));
                    itemSalesPage.setItem(item);
                    itemSalesPage.setSalesPage(page);
                    itemSalesPage.setIsMainItemAndDisplayName(mainFlag, itemDisplayName);
                });
            });
        });

    }

    // 실제론 비활성화 처리
    @Transactional
    public void deletePage(Long pageId) {
        SalesPage page = salesPageRepository.findPageByPageId(pageId).orElseThrow(() -> new RuntimeException());
        page.deletePage(); // 페이지 활성화만 false로 바꾸는걸로 하자
    }

    public List<SalesPage> findSalesPageByItemCategoryAndSearchCond(SearchCond searchCond, Map<String, List<String>> filters, int offset, int limit) {
        return salesPageRepository.findSalesPageByItemCategoryAndSearchCond(searchCond, filters, offset, limit);
    }

}

