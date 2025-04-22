package love.shop.web.item.spec.tv;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.TvSpec;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TvScreenSize { // 화면 크기

    @Id
    @GeneratedValue
    @Column(name = "tv_screen_size_id")
    private Long id;

    private String specName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_spec_id")
    private TvSpec tvSpec;

    public TvScreenSize(String specName, TvSpec tvSpec) {
        this.specName = specName;
        this.tvSpec = tvSpec;
        tvSpec.getTvScreenSizes().add(this);
    }

    /*
        tvPictureQuality.add("24인치");
        tvPictureQuality.add("27인치");
        tvPictureQuality.add("32인치");
        tvPictureQuality.add("40인치");
        tvPictureQuality.add("42인치");
        tvPictureQuality.add("48인치");
        tvPictureQuality.add("50인치");
        tvPictureQuality.add("55인치");
        tvPictureQuality.add("65인치");
        tvPictureQuality.add("70인치");
        tvPictureQuality.add("75인치");
        tvPictureQuality.add("77인치");
        tvPictureQuality.add("83인치");
        tvPictureQuality.add("85인치");
        tvPictureQuality.add("86인치");
        tvPictureQuality.add("97인치");
        tvPictureQuality.add("98인치");
        tvPictureQuality.add("100인치");
        tvPictureQuality.add("115인치");
     */

}
