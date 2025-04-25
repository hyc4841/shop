package love.shop.service.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.item.Item;
import love.shop.domain.itemSalesPage.ItemSalesPage;
import love.shop.domain.salesPage.SalesPage;
import love.shop.repository.ItemPage.SalesPageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PageService {

    public final SalesPageRepository itemPageRepository;

    public void savePage(SalesPage page, List<Item> itemList) {

    }





}
