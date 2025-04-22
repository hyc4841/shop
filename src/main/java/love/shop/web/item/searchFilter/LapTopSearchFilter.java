package love.shop.web.item.searchFilter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import love.shop.web.item.spec.lapTop.*;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LapTopSearchFilter extends SearchFilter {

    private List<LapTopBrand> lapTopBrands;
    private List<LapTopCpu> lapTopCpus;
    private List<LapTopStorage> lapTopStorages;
    private List<LapTopScreenSize> lapTopScreenSizes;
    private List<LapTopManufactureBrand> lapTopManufactureBrands;

    public LapTopSearchFilter(List<LapTopBrand> lapTopBrands, List<LapTopCpu> lapTopCpus, List<LapTopStorage> lapTopStorages, List<LapTopScreenSize> lapTopScreenSizes, List<LapTopManufactureBrand> lapTopManufactureBrands) {
        this.lapTopBrands = lapTopBrands;
        this.lapTopCpus = lapTopCpus;
        this.lapTopStorages = lapTopStorages;
        this.lapTopScreenSizes = lapTopScreenSizes;
        this.lapTopManufactureBrands = lapTopManufactureBrands;
    }

    public static LapTopSearchFilter createLapTopFilter() {
        LapTopSearchFilter lapTopSearchFilter = new LapTopSearchFilter();
        lapTopSearchFilter.setLapTopBrands(Arrays.asList(LapTopBrand.values()));
        lapTopSearchFilter.setLapTopCpus(Arrays.asList(LapTopCpu.values()));
        lapTopSearchFilter.setLapTopStorages(Arrays.asList(LapTopStorage.values()));
        lapTopSearchFilter.setLapTopScreenSizes(Arrays.asList(LapTopScreenSize.values()));
        lapTopSearchFilter.setLapTopManufactureBrands(Arrays.asList(LapTopManufactureBrand.values()));

        return lapTopSearchFilter;
    }



    public LapTopSearchFilter() {

    }
}
