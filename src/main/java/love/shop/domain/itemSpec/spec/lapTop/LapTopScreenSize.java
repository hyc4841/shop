package love.shop.domain.itemSpec.spec.lapTop;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LapTopScreenSize {

    @Id
    @GeneratedValue
    @Column(name = "laptop_screen_size_id")
    private Long id;

    private String specName;
    private Boolean isPopularSpec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptop_spec_id")
    private LapTopSpec lapTopSpec;

    public LapTopScreenSize(String specName, Boolean isPopularSpec, LapTopSpec lapTopSpec) {
        this.specName = specName;
        this.isPopularSpec = isPopularSpec;
        this.lapTopSpec = lapTopSpec;
        lapTopSpec.getLapTopScreenSizes().add(this);
    }
}