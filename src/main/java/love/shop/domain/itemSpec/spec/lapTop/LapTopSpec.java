package love.shop.domain.itemSpec.spec.lapTop;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import love.shop.domain.itemSpec.ItemSpec;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("LapTop")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LapTopSpec extends ItemSpec {

    @Id
    @GeneratedValue
    @Column(name = "laptop_spec_id")
    private Long id;

    private final String dataType = "LapTop";

    @OneToMany(mappedBy = "lapTopSpec", cascade = CascadeType.PERSIST)
    private List<LapTopBrand> lapTopBrands = new ArrayList<>();
    @OneToMany(mappedBy = "lapTopSpec", cascade = CascadeType.PERSIST)
    private List<LapTopCpu> lapTopCpus = new ArrayList<>();
    @OneToMany(mappedBy = "lapTopSpec", cascade = CascadeType.PERSIST)
    private List<LapTopManufactureBrand> lapTopManufactureBrands = new ArrayList<>();
    @OneToMany(mappedBy = "lapTopSpec", cascade = CascadeType.PERSIST)
    private List<LapTopScreenSize> lapTopScreenSizes = new ArrayList<>();
    @OneToMany(mappedBy = "lapTopSpec", cascade = CascadeType.PERSIST)
    private List<LapTopStorage> lapTopStorages = new ArrayList<>();

    public LapTopSpec(String dataType) {
        super(dataType);
    }

    @Override
    public String getDataType() {
        return dataType;
    }
}
