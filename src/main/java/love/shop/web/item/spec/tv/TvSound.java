package love.shop.web.item.spec.tv;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.TvSpec;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TvSound {

    @Id
    @GeneratedValue
    @Column(name = "tv_sound_id")
    private Long id;

    private String specName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_spec_id")
    private TvSpec tvSpec;

    public TvSound(String specName, TvSpec tvSpec) {
        this.specName = specName;
        this.tvSpec = tvSpec;
        tvSpec.getTvSounds().add(this);
    }

    /*
        tvSoundList.add("DTS:X");
        tvSoundList.add("돌비애트모스");
        tvSoundList.add("DTS-VIRTUAL:X");
        tvSoundList.add("dbx-tv");
        tvSoundList.add("공간인식");
        tvSoundList.add("블루투스오디오");
        tvSoundList.add("사운드바동시출력");
        tvSoundList.add("WiSA스피커");
     */


}
