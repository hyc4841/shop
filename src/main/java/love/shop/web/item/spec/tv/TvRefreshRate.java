package love.shop.web.item.spec.tv;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.itemSpec.TvSpec;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TvRefreshRate { // 주사율

     @Id
     @GeneratedValue
     @Column(name = "tv_refresh_rate_id")
     private Long id;

     private String specName;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "tv_spec_id")
     private TvSpec tvSpec;

     public TvRefreshRate(String specName, TvSpec tvSpec) {
          this.specName = specName;
          this.tvSpec = tvSpec;
          tvSpec.getTvRefreshRates().add(this);
     }

     /*
Hz_144, Hz_120, Hz_75, Hz_60
 */
}
