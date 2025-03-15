package love.shop.web.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.domain.item.type.Scanner;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScannerDto extends ItemDto {
    public ScannerDto(Scanner item) {
    }
}
