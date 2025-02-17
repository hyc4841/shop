package love.shop.common.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.FilterExApi;
import love.shop.filter.LogoutFilter;
import love.shop.service.RedisService;
import love.shop.web.login.jwt.JwtTokenProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final FilterExApi filterExApi;

    @Bean
    public FilterRegistrationBean<LogoutFilter> logoutFilter() {
        FilterRegistrationBean<LogoutFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LogoutFilter(jwtTokenProvider, filterExApi, redisService));
        registrationBean.addUrlPatterns("/logout");
        registrationBean.setOrder(1); //  필터 우선순위
        return registrationBean;
    }
}
