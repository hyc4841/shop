package love.shop.web.login.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.FilterExApi;
import love.shop.service.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final FilterExApi filterExApi;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 이 필터는 엑세스 토큰과 리프레시 토큰을 검사하여 해당 유저가 접근 권한이 있는지 확인하는 부분임.
        // 여기서 발생할 수 있는 예외
        // 엑세스 토큰이 만료됨. 엑세스 토큰이 유효하지 않음
        // 리프레시 토큰이 만료됨. 리프레시 토큰이 유효하지 않음

        log.info("JwtFilter 실행");
        // request Header에서 Jwt 토큰 추출
        String token = resolveToken(request); // 엑세스 토큰 추출
        log.info("추출한 엑세스 토큰={}", token);

        if (token != null) {
            try {
                if (jwtTokenProvider.validateToken(token)) { // 토큰이 유효할 경우
                    log.info("토큰이 유효할 경우");
                    // 토큰에서 Autentication 객체를 가져와서 SecurityContext에 저장
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (ExpiredJwtException e) { // 엑세스 토큰이 만료된 경우
                log.info("토큰이 만료된 경우, 리프레시 토큰으로 엑세스 토큰 재발급 시도");

                String refreshToken = jwtTokenProvider.extractRefreshToken(request);
                log.info("리프레시 토큰 추출={}", refreshToken);

                if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                    log.info("리프레시 토큰이 유효하면 새로운 엑세스 토큰을 발급 받는다");

                    // 리프레시 토큰이 유효하면 새로운 엑세스 토큰을 발급 받는다
                    // 왜 응답 헤더에다가 토큰을 넣지?
                    JwtToken newToken = redisService.refreshAccessToken(refreshToken);

                    response.setHeader("AccessToken", newToken.getAccessToken());
                    response.setHeader("RefreshToken", newToken.getRefreshToken());

                    // 새로운 엑세스 토큰으로 SecurityContext 업데이트
                    Authentication authentication = jwtTokenProvider.getAuthentication(newToken.getAccessToken());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // 리프레시 토큰이 유효하지 않는 경우
                    log.info("리프레시 토큰이 유효하지 않은 경우");
                    filterExApi.JwtTokenExHandler(response, "리프레시 토큰이 유효하지 않습니다");
                    return;
                }
            } catch (Exception e) {
                // 다른 예외는 401 응답으로 반환한다.
                // 다른 예외는 토큰 유형, 토큰 보안?, 지원하지 않는 토큰, IllegalArgument 등이 있음
                log.info("error", e);
                filterExApi.JwtTokenExHandler(response, e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // request Header에서 Jwt 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        log.info("resolveToken 실행");
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Map<String, String> extractRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try (BufferedReader bufferedReader = request.getReader()) {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        if (stringBuilder.length() == 0) {
            return Collections.emptyMap();
        }

        return new ObjectMapper().readValue(stringBuilder.toString(), new TypeReference<Map<String, String>>() {});
    }
















}
