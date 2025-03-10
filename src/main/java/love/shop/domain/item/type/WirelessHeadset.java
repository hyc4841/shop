package love.shop.domain.item.type;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.item.Item;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WirelessHeadset extends Item {

    private final String dataType = "WirelessHeadset";

    @Override
    public String getType() {
        return dataType;
    }
}
