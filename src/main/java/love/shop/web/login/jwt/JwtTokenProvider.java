package love.shop.web.login.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.shop.web.login.dto.CustomUser;
import love.shop.web.login.jwt.exception.ApiException;
import love.shop.web.login.jwt.exception.ExceptionCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key; // jwt 서명을 위한 key

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) throws NoSuchAlgorithmException {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes); // 위에서 디코딩된 코드를 key 객체에 넣어주기
    }

    // 유저 정보로 AccessToken과 RefreshToken 생성
    public JwtToken generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        log.info("권한={}", authorities);
        long now = (new Date()).getTime();
        Date issuedAt = new Date();

         Date accessTokenExpire = new Date(now + 1); // 잠시 발급하자마자 만료되는걸로 바꿈
//        Date accessTokenExpire = new Date(System.currentTimeMillis() + 60000); // 1분

        // 토큰 정보에 memberId를 넣기 위해서 가져온다.
        Long memberId = ((CustomUser) authentication.getPrincipal()).getMemberId();

        log.info("authentication.getName()={}", authentication.getName());

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setHeader(createHeaders())
                .setSubject("accessToken")                // 헤더 설정
                .claim("memberId", memberId)            // 토큰 정보에 memberId를 추가
                .claim("iss", "off")                 // 토큰 발급자
                .claim("aud", authentication.getName()) // 토큰 대상자
                .claim("auth", authorities)             // 사용자 권한
                .setExpiration(accessTokenExpire)          // 토큰 만료 시간
                .setIssuedAt(issuedAt)                    // 토큰 발급 시각
                .signWith(key, SignatureAlgorithm.HS256)  // 서명 알고리즘
                .compact();// 토큰 생성

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setHeader(createHeaders()) // Header 부분 설정
                .setSubject("refreshToken") // 토큰 주제 설정
                .claim("memberId", memberId)
                .claim("iss", "off") // 토큰 발급자 설정
                .claim("aud", authentication.getName()) // 토큰 대상자 설정
                .claim("auth", authorities) // 사용자 권한 설정
                .claim("add", "ref") // 추가 정보 설정
                .setExpiration(new Date(now + 86400000)) // 토큰 만료 시간 설정 (1일)
                .setIssuedAt(issuedAt) // 토큰 발급 시각 설정
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘 설정
                .compact();// 토큰 생성

        log.info("토큰 생성 완료");

        // 토큰 객체 생성해서 돌려주기
        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // jwt 토큰을 복호화하여 그 정보를 토대로 Authentication 객체를 만든다.
    public Authentication getAuthentication(String token) {
        // jwt 토큰 복호화
        Claims claims = parseClaims(token);
        log.info("claims={}", claims);
        Object memberId = claims.get("memberId");

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다."); // exception 나중에 api 응답으로 교체하자
        }
        if (memberId == null) {
            throw new RuntimeException("유저 정보가 없는 잘못된 토큰입니다.");
        }

        // 여기 정확히 뭐하는 곳인지 알아야함.
        Collection<? extends  GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        log.info("리프레시 authorities={}", authorities);

        // 지금까지 계속 username에 subject 이름 집어넣고 있었음.

        UserDetails principal = new CustomUser( Long.valueOf(memberId.toString()), claims.getAudience(), "", authorities);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, "", authorities);

        log.info("authenticationToken={}", authenticationToken.getName());
        log.info("authenticationToken={}", authenticationToken);

        return authenticationToken;
    }

    // 토큰 검증 함수
    // 이 함수는 토큰이 유효하면 true를 반환하고 유효하지 않으면 오류를 뱉는다
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().
                    setSigningKey(key). // 서명 키 넣고
                    build().
                    parseClaimsJws(token); // 토큰 파싱해서 유효성 검사
            return true;
            // 위에 코드 분석해서 제대로된 토큰인지 검사하는 과정에서 오류가 발생하면 아래로 넘어간다.
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new ApiException(ExceptionCode.MALFORMED_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), ExceptionCode.TIME_OUT_TOKEN.getMessage());
        } catch (UnsupportedJwtException | IllegalArgumentException exception) {
            throw new ApiException(ExceptionCode.UNSUPPORTED_TOKEN.getMessage());
        } catch (Exception e) {
            throw new ApiException(ExceptionCode.INVALID_TOKEN_INFO.toString());
        }
    }

    // 토큰의 만료시간 가져오기
    public long getExpiration(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
        // 만료된 코인이 들어오면 음수가 된다.
        // 토큰에 저장해 놓은 시간은 보통 미래임. 그런데, 만약 만료되었다면? -> 만료 시간은 과거가 됨. 과거 - 현재 = 마이너스 시간이 된다는 거임.
    }

    // request httponly 쿠키에 담겨있는 리프레시 토큰 추출
    public String extractRefreshToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        return refreshToken;
    }

    // request Header에서 엑세스 토큰 추출
    public String extractAccessToken(HttpServletRequest request) {
        log.info("extractAccessToken 실행");
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // httponly 리프레시 토큰 쿠키
    public Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        cookie.setHttpOnly(true); // javascript로 쿠키에 접근할 수 없도록 설정
        cookie.setSecure(true); // 쿠키가 Https 연결에서만 전송되도록 설정
        cookie.setPath("/"); // 쿠키 경로
        cookie.setAttribute("SameSite", "None"); // 일단 이 설정은 로컬 환경에선 영향이 없었다.

        return cookie;
    }

    // 각종 토큰의 정보 추출
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key) // 키 넣고
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody(); // 토큰 파싱하여 클레임 정보 반환
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 만료된 토큰의 경우 클레임 정보 반환
        }
    }

    // 토큰의 헤더 만들어주는 함수
    private static Map<String, Object> createHeaders() {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");
        return headers;
    }
}

// 리프레시 토큰으로 다시 엑세스 토큰 발급해주는건? -> 이것도 필터에서 한다.
























