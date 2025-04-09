package love.shop.service.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.UserIdOrPasswordNotMatchException;
import love.shop.web.login.jwt.JwtToken;
import love.shop.web.login.jwt.JwtTokenProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate stringRedisTemplate;

    // 로그인
    @Transactional
    public JwtToken login(String loginId, String password, HttpServletResponse response) {
        try {
            // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);
            // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
            // authenticate 매서드가 실행될 때 UserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            // 이 토큰에는 사용자의 정보와 권한 등이 포함됨
            JwtToken tokenInfo = jwtTokenProvider.generateToken(authentication);

            String existRefreshToken = stringRedisTemplate.opsForValue().get(authentication.getName());

            if (existRefreshToken != null) { // 기존에 리프레시 토큰이 있으면 갱신
                stringRedisTemplate.opsForValue().set(authentication.getName(), tokenInfo.getRefreshToken());
            } else {
                stringRedisTemplate.opsForValue().set(authentication.getName(), tokenInfo.getRefreshToken());
            }

            // 리프레시 토큰은 httpOnly 쿠키에 저장한다. 자바스크립트로 접근할 수 없다.
            Cookie refreshTokenCookie = jwtTokenProvider.createRefreshTokenCookie(tokenInfo.getRefreshToken());
            response.addCookie(refreshTokenCookie);

            return tokenInfo;
        } catch (BadCredentialsException e) {
            throw new UserIdOrPasswordNotMatchException("아이디 혹은 비밀번호를 다시 확인하세요.");
        } catch (Exception e) {
            log.error("ex", e);
            throw new RuntimeException("로그인 시도중 에러 발생");
        }

    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

















}
