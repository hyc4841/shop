package love.shop.web.item.spec.tv;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.TvSpec;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TvResolution { // 해상도

    @Id
    @GeneratedValue
    @Column(name = "tv_resolution_id")
    private Long id;
    private String specName;
    private Boolean isPopularSpec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_spec_id")
    private TvSpec tvSpec;

    public TvResolution(String specName, TvSpec tvSpec, Boolean isPopularSpec) {
        this.specName = specName;
        this.tvSpec = tvSpec;
        tvSpec.getTvResolutions().add(this);
        this.isPopularSpec = isPopularSpec;
    }

}
