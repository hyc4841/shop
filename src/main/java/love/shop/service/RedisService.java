package love.shop.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.FilterExApi;
import love.shop.common.exception.RefreshTokenNotExistException;
import love.shop.service.member.MemberService;
import love.shop.web.login.jwt.JwtToken;
import love.shop.web.login.jwt.JwtTokenProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisService {

    private final JwtTokenProvider jwtTokenProvider;
    private MemberService memberService;
    private final StringRedisTemplate redisTemplate;
    private final FilterExApi filterExApi;

    // 리프레시 토큰 저장
    @Transactional
    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set(userId, refreshToken); // userId가 key임
    }

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get(userId);
    }

    // redis에 저장된 리프레시 토큰과 요청 쿠키에 들어있는 리프레시 토큰을 비교해서
    @Transactional
    public JwtToken refreshAccessToken(String refreshToken, HttpServletResponse response) throws IOException {

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        String userName = authentication.getName();
        log.info("username={}", userName);
        String redisRefreshToken = redisTemplate.opsForValue().get(userName); // redis에서 리프레시 토큰 가져오기
        log.info("redis에 저장된 리프레시 토큰={}", redisRefreshToken);

        if (redisRefreshToken != null) { // 리프레시 토큰이 있으면 새로운 토큰 발급
            JwtToken newToken = jwtTokenProvider.generateToken(authentication);

            log.info("새로 만든 리프레시 토큰={}", newToken.getRefreshToken());

            // redis가 내려가서 리프레시 토큰을 저장할 수 없을 때, org.springframework.data.redis.RedisSystemException 이 발생하는데 일단 임시 방편으로
            // try-catch문으로 매꾼다.
            try {
                saveRefreshToken(userName, newToken.getRefreshToken()); // 새로운 리프레시 토큰 저장
            } catch (Exception e) {
                log.error("리프레시 토큰을 저장할 수 없음", e);
                jwtTokenProvider.removeRefreshToken(response);
                filterExApi.jwtTokenExHandler(response, "리프레시 토큰을 저장할 수 없습니다.", 500);
            }

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

    @Transactional
    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(userId);
    }

    @Transactional
    public void addTokenBlackList(String Token) {
        long expiration = jwtTokenProvider.getExpiration(Token); // 엑세스 토큰 만료시간 추출

        log.info("토큰 만료시간={}", expiration);

        if (expiration < 0) { // 만료 시간이 음수이면
            log.info("토큰 만료시간이 음수 입니다. 블랙 리스트 처리할 필요 없음");
            return;
        }

        // 만료 시간 동안만 유효
        // expiration은 토큰이 블랙리스트에 남아 있을 시간임, TimeUnit.MILLISECONDS는 시간 단위임
        redisTemplate.opsForValue().set(Token, "blacklist", expiration, TimeUnit.MILLISECONDS);
        // 토큰을 블랙리스트 시킬 때, 키값은 토큰이 되고, value는 BlackList라고 해야하지 않을까?
    }

    public Boolean isBlackList(String token) {
        return redisTemplate.hasKey(token);// redis에서 키값 검색할 때 사용하는 구문.
    }

    @Transactional
    public void saveDataWithExpire(String key, String value, Long duration) {
        redisTemplate.opsForValue().set(key, value, duration, TimeUnit.SECONDS);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean checkData(String key) {
        return redisTemplate.hasKey(key);
    }

    @Transactional
    public void removeData(String key) {
        redisTemplate.delete(key);
    }


    // 중복되지 않은 코드 생성
    public String generateCode(String email) {
        String code;
        do {
            code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        } while (Boolean.TRUE.equals(redisTemplate.hasKey(code))); // 이메일로 전송하려는 코드가 redis에 있는지 중복 검사
        //       stringRedisTemplate.hasKey(String.valueOf(code));

        saveDataWithExpire(code, email, 60 * 5L); // redis에 저장
        saveDataWithExpire(email, "sent", 60 * 5L);

        return code;
    }

}
