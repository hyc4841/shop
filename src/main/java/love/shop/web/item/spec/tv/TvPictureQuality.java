package love.shop.web.item.spec.tv;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.TvSpec;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TvPictureQuality { // 화질
    @Id
    @GeneratedValue
    @Column(name = "tv_picture_quality_id")
    private Long id;

    private String specName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_spec_id")
    private TvSpec tvSpec;

    public TvPictureQuality(String specName, TvSpec tvSpec) {
        this.specName = specName;
        this.tvSpec = tvSpec;
        tvSpec.getTvPictureQualities().add(this);
    }
}
