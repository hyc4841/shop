package love.shop.web.item.spec.tv;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.TvSpec;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TvProcessor { // 프로세서. 요즘엔 tv에 프로세서 달아주나봄

    @Id
    @GeneratedValue
    @Column(name = "tv_processor_id")
    private Long id;

    private String specName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_spec_id")
    private TvSpec tvSpec;

    public TvProcessor(String specName, TvSpec tvSpec) {
        this.specName = specName;
        this.tvSpec = tvSpec;
        tvSpec.getTvProcessors().add(this);
    }

    /*
    알파7_6세대, 알파5_6세대, 알파9_5세대, 알파7_5세대, 알파5_5세대, 알파9_4세대, 알파7_4세대, 알파9_3세대,
    알파7_3세대, 알파9_2세대, 알파7_2세대, NQ4_AI_Gen2, NQ8_AI_Gen2, NQ8_AI_Gen3, NQ4_AI_Gen3, 뉴럴퀀텀8K,
    뉴럴퀀텀8K_Lite, 뉴럴퀀텀4K,네오퀀텀8K, 네오퀀텀4K
     */
}
