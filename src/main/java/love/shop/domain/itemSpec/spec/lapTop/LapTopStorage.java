package love.shop.domain.itemSpec.spec.lapTop;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LapTopStorage {

    @Id
    @GeneratedValue
    @Column(name = "laptop_storage_id")
    private Long id;

    private String specName;
    private Boolean isPopularSpec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptop_spec_id")
    private LapTopSpec lapTopSpec;

    public LapTopStorage(String specName, Boolean isPopularSpec, LapTopSpec lapTopSpec) {
        this.specName = specName;
        this.isPopularSpec = isPopularSpec;
        this.lapTopSpec = lapTopSpec;
        lapTopSpec.getLapTopStorages().add(this); // 연관관계 메서드
    }
}
