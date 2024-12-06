package love.shop.common.configuration;

import lombok.RequiredArgsConstructor;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SpringSecurityConfig.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("securityFilterChain 실행");
        http.csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않겠다는 설정
                .formLogin(form -> form.disable())
                .logout(logout -> logout.permitAll())
                // 이 부분이 권한 관리 부분
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/signup", "/login").anonymous() // /signup과 /login은 인증이 되지 않은 사용자의 접근을 허용한다는 의미. 즉 인증 없이 경로에 접근할 수 있음
                                .requestMatchers("/member/info/**").hasRole("MEMBER") // /member/**로 들어오는 모든 요청은 MEMBER 권한이 있어야함
                                .anyRequest().permitAll()
                )
                // 필터를 통해 토큰 기반 로그인
                .addFilterBefore(new JwtFilter(jwtTokenProvider, redisService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
