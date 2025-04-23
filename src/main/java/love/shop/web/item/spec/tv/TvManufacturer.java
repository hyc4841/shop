package love.shop.web.item.spec.tv;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.TvSpec;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TvManufacturer { // 제조사

    @Id
    @GeneratedValue
    @Column(name = "tv_manufacturer_id")
    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_spec_id")
    private TvSpec tvSpec;

    public TvManufacturer(String specName, TvSpec tvSpec, Boolean isPopularSpec) {
        this.specName = specName;
        this.tvSpec = tvSpec;
        tvSpec.getTvManufacturers().add(this);
        this.isPopularSpec = isPopularSpec;
    }
}
