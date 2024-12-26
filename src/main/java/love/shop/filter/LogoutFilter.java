package love.shop.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.FilterExApi;
import love.shop.service.RedisService;
import love.shop.web.login.jwt.JwtTokenProvider;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 로그아웃 필터
 */
@Slf4j
@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final FilterExApi filterExApi;
    private final RedisService redisService;

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("로그아웃 필터 시작");
        /*
        로그아웃 요청으로 들어온 엑세스 토큰을 먼저 검사 => 만료되거나 유효하지 않다.
        => 요청 쿠키에 들어 있는 리프레시 토큰을 확인, 없으면 로그아웃 요청 무시, 있으면
        유효성 검사 후 블랙 리스트에 추가
        */

        /*
        1. 요청에 토큰이 있는지
        2. 이 토큰이 유효한 토큰인지
        3. 이 토큰이 블랙 리스트에 들어 있는 토큰인지
        */

        String accessToken = jwtTokenProvider.extractAccessToken(request);
        String refreshToken = jwtTokenProvider.extractRefreshToken(request);

        // 1. 요청에 토큰 있는지 검사
        if (refreshToken == null && accessToken == null) {
            filterExApi.jwtTokenExHandler(response, "해당 요청으로 로그아웃을 처리할 수 없습니다. 요청에 토큰 정보를 찾을 수가 없음", 403);
            log.info("해당 요청으로 로그아웃을 처리할 수 없습니다. 요청에 토큰 정보를 찾을 수가 없음");
            return;
        }
        // 2. 해당 토큰이 이미 블랙 리스트 처리 된 토큰인지 검사
        if (redisService.isBlackList(accessToken) && redisService.isBlackList(refreshToken)) {
            filterExApi.jwtTokenExHandler(response, "이미 로그아웃 처리된 사용자 입니다. 토큰 중에 블랙 리스트 처리된 것이 있음.", 400);
            log.info("이미 로그아웃 처리된 사용자 입니다. 토큰 중에 블랙 리스트 처리된 것이 있음.");
            return;
        }

        // 3. 토큰의 유효성 검사
        try {
            if (jwtTokenProvider.validateToken(accessToken)) {
                // 아직 유효한 토큰이면 블랙 리스트에 추가
                log.info("엑세스 토큰이 유효 하므로 블랙 리스트에 추가");
                redisService.addTokenBlackList(accessToken);
            }
            // 엑세스 토큰이 유효한 경우에도 리프레시 토큰 블랙 리스트 처리해 주어야 하니까.
            try {
                if (jwtTokenProvider.validateToken(refreshToken)) {
                    log.info("리프레시 토큰이 유효 하므로 블랙 리스트에 추가");
                    redisService.addTokenBlackList(refreshToken);
                }
            } catch (ExpiredJwtException e) {
                log.info("해당 요청으로 로그아웃 처리할 수 없습니다. 리프레시 토큰이 만료되거나 유효하지 않은 경우");
                filterExApi.jwtTokenExHandler(response, "해당 요청으로 로그아웃 처리할 수 없습니다. 리프레시 토큰이 만료되거나 유효하지 않은 경우", 403);
                return;
            }
        } catch (ExpiredJwtException e) {
            // 엑세스 토큰이 만료된 경우, 엑세스 토큰은 블랙 리스트에 따로 추가하지 않는다. 어차피 유효성이 없는 코인이니까
            log.info("엑세스 토큰이 유효 하지 않으므로");
            try {
                // 리프레시 토큰 유효성 검사
                if (jwtTokenProvider.validateToken(refreshToken)) {
                    // 아직 유효한 토큰이면 블랙 리스트에 추가
                    log.info("리프레시 토큰이 유효 하므로 블랙 리스트에 추가");
                    redisService.addTokenBlackList(refreshToken);
                }
            } catch (ExpiredJwtException err) {
                filterExApi.jwtTokenExHandler(response, "해당 요청으로 로그아웃 처리할 수 없습니다. 리프레시 토큰이 만료되거나 유효하지 않은 경우", 403);
                log.info("해당 요청으로 로그아웃 처리할 수 없습니다. 리프레시 토큰이 만료되거나 유효하지 않은 경우");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
