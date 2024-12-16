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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

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
                log.info("토큰이 만료된 경우");
                // request 바디에서 리프레시 토큰을 추출한다?
                Map<String, String> body = extractRequestBody(request);
                String refreshToken = body.get("refreshToken");

                if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                    log.info(" 리프레시 토큰이 유효하면 새로운 엑세스 토큰을 발급 받는다");

                    // 리프레시 토큰이 유효하면 새로운 엑세스 토큰을 발급 받는다
                    JwtToken newToken = redisService.refreshAccessToken(refreshToken);
                    response.setHeader("AccessToken", newToken.getAccessToken());
                    response.setHeader("RefreshToken", newToken.getRefreshToken());

                    // 새로운 엑세스 토큰으로 SecurityContext 업데이트
                    Authentication authentication = jwtTokenProvider.getAuthentication(newToken.getAccessToken());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // 리프레시 토큰이 유효하지 않는 경우
                    log.info("리프레시 토큰이 유효하지 않은 경우?");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("리프레시 토큰이 유효하지 않거나 유효기간이 지났습니다.");
                    response.getWriter().flush();
                    return;
                }
            } catch (Exception e) {
                // 다른 예외는 401 응답으로 반환한다.
                log.info("error", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다");
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
