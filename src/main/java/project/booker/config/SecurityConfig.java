package project.booker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
     * PasswordEncoder를 Bean으로 등록(pw 암호화)
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
        CSRF: 악의적인 웹사이트나 이메일을 통해 인증된 사용자의 권한을 이용하여 권한이 필요한 요청을 보내는 공격이다. JWT 인증 인가 구현시
              해당 토큰을 CSRF로 오해 할 수 있기 때문에 비활성화 시킨다.

        httpBasic: HTTP Basic Authentication은 사용자가 요청을 보낼 때 인증 정보를 요청 헤더에 Base64로 인코딩하여 전송하는 간단한 인증 방식입니다.
                   그러나 각 요청마다 인증 정보를 포함해야 하며, Base64로 인코딩되어 있어 디코딩이 가능하여 보안에 취약합니다.
                   JWT로 인증 인가를 구현하면 굳이 HttpBasic을 사용할 이유가 없다.
    */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf((csrf) -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin((formLogin) -> formLogin.disable())
                .httpBasic((httpBasic) -> httpBasic.disable());
        return http.build();
    }

}
