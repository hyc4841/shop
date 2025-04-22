package love.shop.web.item.spec.tv;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.TvSpec;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TvSpeakerOutput {

    @Id
    @GeneratedValue
    @Column(name = "tv_speaker_output_id")
    private Long id;

    private String specName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_spec_id")
    private TvSpec tvSpec;

    public TvSpeakerOutput(String specName, TvSpec tvSpec) {
        this.specName = specName;
        this.tvSpec = tvSpec;
        tvSpec.getTvSpeakerOutputs().add(this);
    }

    /*
        tvSoundList.add("20W");
        tvSoundList.add("40W");
        tvSoundList.add("60W");
     */

}
