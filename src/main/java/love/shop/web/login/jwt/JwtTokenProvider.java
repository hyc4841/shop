package love.shop.web.login.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import love.shop.web.login.jwt.exception.ApiException;
import love.shop.web.login.jwt.exception.ExceptionCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key; // jwt 서명을 위한 key


    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Base64로 인코딩된 secret key를 디코딩 하는 과정
        this.key = Keys.hmacShaKeyFor(keyBytes); // 위에서 디코딩된 코드를 key 객체에 넣어주기
    }

    // 유저 정보로 AccessToken과 RefreshToken 생성
    public JwtToken generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date issuedAt = new Date();

        // Date accessTokenExpire = new Date(now + 1800000); // 30분
        Date accessTokenExpire = new Date(System.currentTimeMillis() + 6000); // 1분

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setHeader(createHeaders())
                .setSubject("accessToken")                // 헤더 설정
                .claim("iss", "off")                 // 토큰 발급자
                .claim("aud", authentication.getName()) // 토큰 대상자
                .claim("auth", authorities)             // 사용자 권한
                .setExpiration(accessTokenExpire)   // 토큰 만료 시간
                .setIssuedAt(issuedAt)                    // 토큰 발급 시각
                .signWith(key, SignatureAlgorithm.ES256)  // 서명 알고리즘
                .compact();// 토큰 생성

        String refreshToken = Jwts.builder()
                .setHeader(createHeaders()) // Header 부분 설정
                .setSubject("refreshToken") // 토큰 주제 설정
                .claim("iss", "off") // 토큰 발급자 설정
                .claim("aud", authentication.getName()) // 토큰 대상자 설정
                .claim("auth", authorities) // 사용자 권한 설정
                .claim("add", "ref") // 추가 정보 설정
                .setExpiration(new Date(now + 86400000)) // 토큰 만료 시간 설정 (1일)
                .setIssuedAt(issuedAt) // 토큰 발급 시각 설정
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘 설정
                .compact();// 토큰 생성

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

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends  GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication return
//        User principal = new User((String) claims.get("aud"), "", authorities);

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // 토큰 파싱해서 유효성 검사
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new ApiException(ExceptionCode.INVALID_TOKEN_INFO.toString());
        } catch (ExpiredJwtException e) {
            throw new ApiException(ExceptionCode.TIME_OUT_TOKEN.toString());
        } catch (UnsupportedJwtException | IllegalArgumentException exception) {
            throw new ApiException(ExceptionCode.INVALID_TOKEN_INFO.toString());
        } catch (Exception e) {
            throw new ApiException(ExceptionCode.INVALID_TOKEN_INFO.toString());
        }
    }

    public long getExpiration(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(accessToken).getBody(); // 토큰 파싱하여 클레임 정보 반환
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 만료된 토큰의 경우 클레임 정보 반환
        }
    }


    private static Map<String, Object> createHeaders() {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");
        return headers;
    }
}
























