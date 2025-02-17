package love.shop.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.ErrorApi;
import love.shop.common.exception.FilterExApi;
import love.shop.service.RedisService;
import love.shop.web.login.jwt.JwtToken;
import love.shop.web.login.jwt.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final FilterExApi filterExApi;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtFilter 실행");
        
        log.info("request.getRequestURI()={}", request.getRequestURI());
        log.info("request.getRequestURL()={}", request.getRequestURL());

        if (request.getRequestURI().equals("/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 이 필터는 엑세스 토큰과 리프레시 토큰을 검사하여 해당 유저가 접근 권한이 있는지 확인하는 부분임.
        // 여기서 발생할 수 있는 예외
        // 엑세스 토큰이 만료됨. 엑세스 토큰이 유효하지 않음
        // 리프레시 토큰이 만료됨. 리프레시 토큰이 유효하지 않음

        // request Header에서 Jwt 토큰 추출
        String token = jwtTokenProvider.extractAccessToken(request); // 엑세스 토큰 추출
        log.info("추출한 엑세스 토큰={}", token);

        if (token.equals("null")) {
            log.info("token은 null임={}", token);
        } else {
            log.info("token은 null이 아님={}", token);
        }

        if (!token.equals("null")) {
            // 토큰 유효성 검사하기 전에 해당 토큰이 블랙리스트에 있는지 없는지 검사
            if (redisService.isBlackList(token)) {
                log.info("블랙 리스트에 있는 토큰임={}", token);
                filterExApi.jwtTokenExHandler(response, "블랙 리스트 처리된 토큰입니다. 유효한 접근이 아님", 401);
                return;
            }

            try {


                if (jwtTokenProvider.validateToken(token)) { // 토큰이 유효할 경우
                    log.info("토큰이 유효할 경우");
                    // 토큰에서 Autentication 객체를 가져와서 SecurityContext에 저장
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    // SecurityContextHolder에 유저 권한 정보를 입력하는 부분. 이 부분이 나중에
                    // UsernamePasswordAuthenticationFilter에서 권한정보 읽을때 사용되나 봄.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (ExpiredJwtException e) { // 엑세스 토큰이 만료된 경우
                log.info("토큰이 만료된 경우, 리프레시 토큰으로 엑세스 토큰 재발급 시도");
                // 현재 리프레시 토큰을 통한 엑세스 토큰 재발급은 리프레시 토큰의 재발급도 같이 실시되고 있음.

                String refreshToken = jwtTokenProvider.extractRefreshToken(request);
                log.info("리프레시 토큰 추출={}", refreshToken);

                if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                    log.info("리프레시 토큰이 유효하면 새로운 엑세스 토큰을 발급 받는다");

                    // 리프레시 토큰이 유효하면 새로운 엑세스 토큰을 발급 받는다.
                    JwtToken newToken = redisService.refreshAccessToken(refreshToken, response); // 여기서 리프레시 토큰의 유효성도 검사된다.

//                    response.setHeader("RefreshToken", newToken.getRefreshToken()); // 사실 여기서 리프레시 토큰
                    // 새로운 엑세스 토큰
                    response.setHeader("Access-Token", newToken.getAccessToken());

                    // 리프레시 토큰도 재발급 해준다.
                    Cookie refreshTokenCookie = jwtTokenProvider.createRefreshTokenCookie(newToken.getRefreshToken());
                    response.addCookie(refreshTokenCookie);

                    // 새로운 엑세스 토큰으로 SecurityContext 업데이트
                    Authentication authentication = jwtTokenProvider.getAuthentication(newToken.getAccessToken());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // 리프레시 토큰이 유효하지 않는 경우
                    log.info("리프레시 토큰이 유효하지 않은 경우");
                    filterExApi.jwtTokenExHandler(response, "리프레시 토큰이 유효하지 않습니다", 401);
                    return;
                }
            } catch (Exception e) {
                // 다른 예외는 401 응답으로 반환한다.
                // 다른 예외는 토큰 유형, 토큰 보안?, 지원하지 않는 토큰, IllegalArgument 등이 있음
                log.info("error", e);

                // jwtTokenProvider에게 리프레시 토큰을 제거하도록 한다. 이 부분은 작동함. 즉 필터에서 직접 삭제 안하고 다른 객체에 전달해서 삭제할 수 있음.
                jwtTokenProvider.removeRefreshToken(response);

                // 예외 api 응답으로 보내기
                filterExApi.jwtTokenExHandler(response, e.getMessage(), 401);

                return;
            }
        } else {
            log.info("비 로그인 유저");
        }

        filterChain.doFilter(request, response);
    }


}
