package love.shop.common.configuration;

import lombok.RequiredArgsConstructor;
import love.shop.common.exception.FilterExApi;
import love.shop.service.RedisService;
import love.shop.web.login.jwt.JwtFilter;
import love.shop.web.login.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SpringSecurityConfig.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final FilterExApi filterExApi;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("securityFilterChain 실행");
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않겠다는 설정
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable())
                // 이 부분이 권한 관리 부분
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/signup", "/login").anonymous() // /signup과 /login은 인증이 되지 않은 사용자의 접근을 허용한다는 의미. 즉 인증 없이 경로에 접근할 수 있음
                                .requestMatchers("/member/**").hasRole("MEMBER") // /member/**로 들어오는 모든 요청은 MEMBER 권한이 있어야함
                                .anyRequest().permitAll()
                )
                // 필터를 통해 토큰 기반 로그인
                .addFilterBefore(new JwtFilter(jwtTokenProvider, redisService, filterExApi), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 허용할 출처
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 쿠키 및 인증 정보를 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 엔드포인트에 대해 CORS 설정

        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
