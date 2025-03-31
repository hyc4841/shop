package love.shop.service.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.item.Item;
import love.shop.domain.itemPage.ItemPage;
import love.shop.domain.page.Page;
import love.shop.repository.ItemPage.ItemPageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PageService {

    public final ItemPageRepository itemPageRepository;

    public void savePage(Page page, List<Item> itemList) {

        ItemPage itemPage1 = new ItemPage("이건 책");
        itemPage1.setPage(page);
        itemPage1.setItem(itemList.get(0));

        ItemPage itemPage2 = new ItemPage("이건 노트북");
        itemPage2.setPage(page);
        itemPage2.setItem(itemList.get(1));

        itemPageRepository.savePage(page);
    }





}
