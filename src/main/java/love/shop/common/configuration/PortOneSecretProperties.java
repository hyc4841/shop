package love.shop.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("portone.secret")
public class PortOneSecretProperties {
    private String api;
    private String webhook;
    private String storeId;
}
