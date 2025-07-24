package love.shop.domain.itemSpec.spec.tv;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TvRefreshRate { // 주사율

     @Id
     @GeneratedValue
     @Column(name = "tv_refresh_rate_id")
     private Long id;
     private String specName;
     private Boolean isPopularSpec;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "tv_spec_id")
     private TvSpec tvSpec;

     public TvRefreshRate(String specName, TvSpec tvSpec, Boolean isPopularSpec) {
          this.specName = specName;
          this.tvSpec = tvSpec;
          tvSpec.getTvRefreshRates().add(this);
          this.isPopularSpec = isPopularSpec;
     }

}
