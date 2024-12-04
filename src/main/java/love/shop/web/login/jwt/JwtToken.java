package love.shop.web.login.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@AllArgsConstructor
@RedisHash(value = "jwtToken", timeToLive = 60)
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
