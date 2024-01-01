package project.booker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import project.booker.interceptor.CreateJwtFilter;
import project.booker.interceptor.JwtAuthorizationFilter;
import project.booker.interceptor.VerifyUserFilter;
import project.booker.service.LoginService.LoginService;
import project.booker.util.jwt.JwtProvider;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginService loginService;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**") // cors를 적용할 spring서버의 url 패턴.
                .allowedOrigins("http://localhost:3000") // cors를 허용할 도메인. 제한을 모두 해제하려면 "**"
                .allowedMethods("OPTIONS","GET","POST","PUT","DELETE") // cors를 허용할 method
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new VerifyUserFilter(loginService, objectMapper))
                .order(1)
                .addPathPatterns("/login");

        registry.addInterceptor(new CreateJwtFilter(loginService, objectMapper, jwtProvider))
                .order(2)
                .addPathPatterns("/login");

        registry.addInterceptor(new JwtAuthorizationFilter(jwtProvider, objectMapper))
                .order(3)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/auth/refresh/token", "/signup", "/profile", "/oauth2/code/naver", "/oauth2/code/google");
    }

}
