package love.shop.web.item.filter.tv;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import love.shop.domain.itemSpec.TVSpec;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TVDisplayType { // 화면 종류
    @Id
    @GeneratedValue
    @Column(name = "tv_display_type_id")
    private Long id;

    private String specName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_spec_id")
    private TVSpec tvSpec;

}
