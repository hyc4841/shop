package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.Printer;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrinterDto extends ItemDto {
    public PrinterDto(Printer item) {
    }
}
