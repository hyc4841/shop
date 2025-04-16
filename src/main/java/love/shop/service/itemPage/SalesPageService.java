package love.shop.service.itemPage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.item.Item;
import love.shop.domain.itemOption.ItemOption;
import love.shop.domain.itemSalesPage.ItemSalesPage;
import love.shop.domain.salesPage.SalesPage;
import love.shop.repository.ItemPage.SalesPageRepository;
import love.shop.repository.item.ItemRepository;
import love.shop.web.item.searchCond.SearchCond;
import love.shop.web.itemOption.dto.CreateItemOptionReqDto;
import love.shop.web.itemPage.dto.CreatePageReqDto;
import love.shop.web.page.dto.SalesPageDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

        SalesPage salesPage = new SalesPage(pageReqDto.getPageName(), pageReqDto.getImages(), pageReqDto.getDescription());
        // ItemOption 설정
        List<CreateItemOptionReqDto> optionList = pageReqDto.getOptionList();

        Iterator<CreateItemOptionReqDto> iterator = optionList.iterator(); // 만약에 리스트 요소 삭제하는 방식 사용할거면 iterator 사용해야함.


        // 먼저 전부 ItemOption 엔티티로 만든다
        // 현재 요소의 이름과 부모의 이름 요소가 같은 엔티티들을 현재 요소의 자식으로 넣는다(자식들도 마찬가지로 현재 요소를 부모로 넣는다.)
        // 자식 요소도 마찬가지로 자식이 또 있을 수 있으므로 재귀 함수로 위 과정을 반복한다.

        // 먼저 최상위 옵션들을 뽑아낸다.
        for (CreateItemOptionReqDto itemOptionDto : optionList) {
            if (itemOptionDto.getParentNum() == null) { // 부모가 없는 옵션은 최상위 옵션이다
                ItemOption itemOption = new ItemOption(itemOptionDto.getOptionName(), itemOptionDto.getOptionNum()); // 최상위 ItemOption 엔티티 생성

                if (itemOptionDto.getItemId() != null) {
                    Item item = itemRepository.findOne(itemOptionDto.getItemId()).orElseThrow(() -> new RuntimeException("해당 아이템이 없음"));
                    itemOption.setItem(item);
                }
//                optionList.remove(itemOptionDto); // 중간에 리스트 객체를 제거하면 문제가 생기나?
                itemOption.setSalesPage(salesPage);
                makeItemOptionDepth(optionList, itemOption, salesPage); // 재귀 함수 부분

            }
        }

//        itemPageReg(salesPage, pageReqDto.getOptionAndItem());

        // 일단 다 설정 됐는데 salesPage 저장 시점에 같이 저장되는지 봐야함
        log.info("판매 페이지 저장 시점");
        salesPageRepository.savePage(salesPage);

        return salesPage;
    }

    private void makeItemOptionDepth(List<CreateItemOptionReqDto> optionDtoList, ItemOption parent, SalesPage salesPage) {
        for (CreateItemOptionReqDto itemOptionDto : optionDtoList) {
            if (Objects.equals(parent.getOptionNum(), itemOptionDto.getParentNum())) {

                ItemOption itemOption = new ItemOption(itemOptionDto.getOptionName(), itemOptionDto.getOptionNum());

                if (itemOptionDto.getItemId() != null) {
                    log.info("아이템 조회 시점");
                    Item item = itemRepository.findOne(itemOptionDto.getItemId()).orElseThrow(() -> new RuntimeException("해당 아이템이 존재하지 않음"));
                    itemOption.setItem(item);
                }
                itemOption.setParent(parent); // 부모 설정(부모 쪽에도 자식이 설정됨)
                itemOption.setSalesPage(salesPage); // 판매 페이지 설정(판매 페이지 쪽에도 itemOption 이 설정됨

//                optionDtoList.remove(itemOptionDto);
                makeItemOptionDepth(optionDtoList, itemOption, salesPage);
            }
        }

    }



    // 아이템의 카테고리로 아이템 페이지 찾기
    public List<SalesPageDto> findItemPageWithItemCategory(Long categoryId, int offset, int limit) {

        List<SalesPage> pageByItemCategory = salesPageRepository.findPageByItemCategory(categoryId, offset, limit);

        return SalesPageDto.createPageDtoList(pageByItemCategory);
    }

    // 페이지 단건 조회
    public SalesPageDto findPageByPageId(Long pageId) {
        SalesPage salesPage = salesPageRepository.findPageByPageId(pageId).orElseThrow(() -> new RuntimeException());
        return new SalesPageDto(salesPage);
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

