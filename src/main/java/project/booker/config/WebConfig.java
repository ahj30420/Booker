package project.booker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import project.booker.interceptor.CreateJwtFilter;
import project.booker.interceptor.JwtAuthorizationFilter;
import project.booker.interceptor.VerifyUserFilter;
import project.booker.service.loginService.LoginService;
import project.booker.util.jwt.JwtProvider;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginService loginService;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new VerifyUserFilter(loginService, objectMapper))
                .order(1)
                .addPathPatterns("/login");

        registry.addInterceptor(new CreateJwtFilter(loginService, objectMapper, jwtProvider))
                .order(2)
                .addPathPatterns("/login");

        registry.addInterceptor(new JwtAuthorizationFilter(jwtProvider, objectMapper))
                .order(2)
                .excludePathPatterns("/login", "/auth/refresh/token", "/signup");
    }

}
