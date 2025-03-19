package love.shop.web.item.searchCond;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import love.shop.web.item.filter.lapTop.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeName(value = "LapTop")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LapTopSearchCond extends SearchCond {

    private List<LapTopBrand> lapTopBrands;
    private List<LapTopCpu> lapTopCpus;
    private List<LapTopStorage> lapTopStorages;
    private List<LapTopScreenSize> lapTopScreenSizes;
    private List<LapTopManufactureBrand> lapTopManufactureBrands;

    public LapTopSearchCond(String type, Long categories, String itemName, Integer morePrice, Integer lessPrice, List<LapTopBrand> lapTopBrands, List<LapTopCpu> lapTopCpus, List<LapTopStorage> lapTopStorages, List<LapTopScreenSize> lapTopScreenSizes, List<LapTopManufactureBrand> lapTopManufactureBrands) {
        super(type, categories, itemName, morePrice, lessPrice);
        this.lapTopBrands = lapTopBrands;
        this.lapTopCpus = lapTopCpus;
        this.lapTopStorages = lapTopStorages;
        this.lapTopScreenSizes = lapTopScreenSizes;
        this.lapTopManufactureBrands = lapTopManufactureBrands;
    }
}
