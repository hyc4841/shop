package love.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.web.login.jwt.JwtToken;
import love.shop.web.login.jwt.JwtTokenProvider;
import love.shop.web.login.jwt.repository.TokenRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final JwtTokenProvider jwtTokenProvider;
    private MemberService memberService;
    private final TokenRepository tokenRepository;
    private final StringRedisTemplate stringRedisTemplate;

    // 리프레시 토큰 저장
    public void saveRefreshToken(String userId, String refreshToken) {
        stringRedisTemplate.opsForValue().set(userId, refreshToken); // userId가 key임
    }

    public JwtToken refreshAccessToken(String accessToken) {

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        String userName = authentication.getName();
        String redisRefreshToken = stringRedisTemplate.opsForValue().get(userName); // redis에서 리프레시 토큰 가져오기

        if (redisRefreshToken != null) { // 리프레시 토큰이 있으면 새로운 토큰 발급
            JwtToken newToken = jwtTokenProvider.generateToken(authentication);

            saveRefreshToken(userName, newToken.getRefreshToken()); // 새로운 리프레시 토큰 저장

            return JwtToken.builder()
                    .grantType("Bearer")
                    .accessToken(newToken.getAccessToken())
                    .refreshToken(null)
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
        }
    }

    public void deleteRefreshToken(String userId) {
        stringRedisTemplate.delete(userId);
    }

    public void blacklistAccessToken(String accessToken) {
        long expiration = jwtTokenProvider.getExpiration(accessToken); // 엑세스 토큰 만료시간 추출
        // Access Token을 키로 하고 "logout"을 값으로 하여 Redis에 저장
        // 만료 시간 동안만 유효
        stringRedisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }

}
