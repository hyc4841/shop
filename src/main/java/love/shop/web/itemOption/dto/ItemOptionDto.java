package love.shop.web.itemOption.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.item.Item;
import love.shop.domain.itemOption.ItemOption;
import love.shop.web.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Data
public class ItemOptionDto {

    private Long id;
    private Long parentId;
    private String optionName;
    private ItemDto item;
    private Long salesPageId;
    private Boolean isMainItem;
    private List<ItemOptionDto> child;

    // 자식은 연결 안된 생성자
    public ItemOptionDto(ItemOption itemOption) {
        this.id = itemOption.getId();
        this.optionName = itemOption.getOptionName();

        if (itemOption.getParent() != null) {
            this.parentId = itemOption.getParent().getId();
        } else {
            this.parentId = null;
        }

        if (itemOption.getItem() != null) {
            log.info("아이템은 dto로 변환 안되냐?={}", itemOption.getItem());
            this.item = ItemDto.createItemDto(Item.proxyToEntity(itemOption.getItem()));
        } else {
            this.item = null;
        }
        this.salesPageId = itemOption.getSalesPage().getId();
        this.isMainItem = itemOption.getIsMainItem();
    }

    public static List<ItemOptionDto> makeItemOptionDto(List<ItemOption> itemOptionList) {

        List<ItemOptionDto> parentList = new ArrayList<>();

        for (ItemOption itemOption : itemOptionList) {
            if (itemOption.getParent() == null) {
                ItemOptionDto parentItemOptionDto = makeDepth(itemOptionList, itemOption);
                parentList.add(parentItemOptionDto);
            }
        }

        return parentList;
    }

    private static ItemOptionDto makeDepth(List<ItemOption> itemOptionList, ItemOption parent) {
        List<ItemOptionDto> childList = new ArrayList<>();

        ItemOptionDto parentDto = new ItemOptionDto(parent);

        for (ItemOption itemOption : itemOptionList) {
            if (itemOption.getParent() != null && Objects.equals(itemOption.getParent().getId(), parent.getId())) {
                log.info("부모 id={}", itemOption.getParent());
                ItemOptionDto child = makeDepth(itemOptionList, itemOption);
                childList.add(child);
            }
        }

        parentDto.setChild(childList);
        return parentDto;
    }


    public void setChild(List<ItemOptionDto> child) {
        this.child = child;
    }

}
