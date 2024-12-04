package love.shop.common.configuration;

import lombok.RequiredArgsConstructor;
import love.shop.service.RedisService;
import love.shop.web.login.jwt.JwtFilter;
import love.shop.web.login.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize ->

                        authorize.anyRequest().permitAll())
                .formLogin(form -> form.disable())
                .logout(logout -> logout.permitAll());

        // 필터를 통해 토큰 기반 로그인
        http.addFilterBefore(new JwtFilter(jwtTokenProvider, redisService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
