package love.shop.web.item.searchCond;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonTypeName(value = "LapTop")
@EqualsAndHashCode(callSuper = true)
@Data
public class LapTopSearchCond extends SearchCond{

    private String lapTopBrand;
    private String lapTopCpu;
    private String lapTopStorage;
    private String lapTopScreenSize;
    private String lapTopManufactureBrand;
}
