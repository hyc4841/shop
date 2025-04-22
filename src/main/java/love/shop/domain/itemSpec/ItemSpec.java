package love.shop.domain.itemSpec;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class ItemSpec {

    @Id
    @GeneratedValue
    @Column(name = "item_spec_id")
    private Long id;

    private String dataType;

    public abstract String getDataType();

    public ItemSpec(String dataType) {
        this.dataType = dataType;
    }
}
