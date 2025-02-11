package love.shop.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.FilterExApi;
import love.shop.common.exception.RefreshTokenNotExistException;
import love.shop.service.member.MemberService;
import love.shop.web.login.jwt.JwtToken;
import love.shop.web.login.jwt.JwtTokenProvider;
import love.shop.web.login.jwt.repository.TokenRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final JwtTokenProvider jwtTokenProvider;
    private MemberService memberService;
    private final StringRedisTemplate stringRedisTemplate;
    private final FilterExApi filterExApi;

    // 리프레시 토큰 저장
    public void saveRefreshToken(String userId, String refreshToken) {
        stringRedisTemplate.opsForValue().set(userId, refreshToken); // userId가 key임
    }

    public String getRefreshToken(String userId) {
        return stringRedisTemplate.opsForValue().get(userId);
    }

    // redis에 저장된 리프레시 토큰과 요청 쿠키에 들어있는 리프레시 토큰을 비교해서
    public JwtToken refreshAccessToken(String refreshToken, HttpServletResponse response) throws IOException {

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        String userName = authentication.getName();
        log.info("username={}", userName);
        String redisRefreshToken = stringRedisTemplate.opsForValue().get(userName); // redis에서 리프레시 토큰 가져오기
        log.info("redis에 저장된 리프레시 토큰={}", redisRefreshToken);

        if (redisRefreshToken != null) { // 리프레시 토큰이 있으면 새로운 토큰 발급
            JwtToken newToken = jwtTokenProvider.generateToken(authentication);

            log.info("새로 만든 리프레시 토큰={}", newToken.getRefreshToken());

            saveRefreshToken(userName, newToken.getRefreshToken()); // 새로운 리프레시 토큰 저장

            return JwtToken.builder()
                    .grantType("Bearer")
                    .accessToken(newToken.getAccessToken())
                    .refreshToken(newToken.getRefreshToken())
                    .build();
        } else { // redisRefreshToken == null.
            // 엑세스 코인은 있는데 리프레시 코인이 redis에 저장이 안되어 있음. 문제가 있음.
            log.info("redis에 저장되지 않은 리프레시 토큰 발견");
            filterExApi.jwtTokenExHandler(response, "redis에 존재하지 않은 리프레시 토큰입니다.", 401);
            jwtTokenProvider.removeRefreshToken(response);
            throw new RefreshTokenNotExistException("redis에 존재하지 않은 리프레시 토큰입니다.");
        }
    }

    public void deleteRefreshToken(String userId) {
        stringRedisTemplate.delete(userId);
    }

    public void addTokenBlackList(String Token) {
        long expiration = jwtTokenProvider.getExpiration(Token); // 엑세스 토큰 만료시간 추출

        log.info("토큰 만료시간={}", expiration);

        if (expiration < 0) { // 만료 시간이 음수이면
            log.info("토큰 만료시간이 음수 입니다. 블랙 리스트 처리할 필요 없음");
            return;
        }

        // 만료 시간 동안만 유효
        // expiration은 토큰이 블랙리스트에 남아 있을 시간임, TimeUnit.MILLISECONDS는 시간 단위임
        stringRedisTemplate.opsForValue().set(Token, "blacklist", expiration, TimeUnit.MILLISECONDS);
        // 토큰을 블랙리스트 시킬 때, 키값은 토큰이 되고, value는 BlackList라고 해야하지 않을까?
    }

    public Boolean isBlackList(String token) {
        return stringRedisTemplate.hasKey(token);// redis에서 키값 검색할 때 사용하는 구문.
    }

}
