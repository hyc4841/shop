package love.shop.web.item.spec.tv;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.TvSpec;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TvSpeakerChannel {

    @Id
    @GeneratedValue
    @Column(name = "tv_speaker_channel_id")
    private Long id;

    private String specName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_spec_id")
    private TvSpec tvSpec;

    public TvSpeakerChannel(String specName, TvSpec tvSpec) {
        this.specName = specName;
        this.tvSpec = tvSpec;
        tvSpec.getTvSpeakerChannels().add(this);
    }
    /*
        tvSoundList.add("2.0채널");
        tvSoundList.add("2.0.2채널");
        tvSoundList.add("2.1채널");
        tvSoundList.add("2.1.2채널");
        tvSoundList.add("3.1.2채널");
        tvSoundList.add("4.0채널");
        tvSoundList.add("4.1채널");
        tvSoundList.add("4.2채널");

     */

}
