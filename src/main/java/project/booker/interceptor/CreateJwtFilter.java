package project.booker.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import project.booker.dto.AuthenticatedUser;
import project.booker.service.LoginService.LoginService;
import project.booker.util.jwt.Jwt;
import project.booker.util.jwt.JwtProvider;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CreateJwtFilter implements HandlerInterceptor {

    private final LoginService loginService;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;

    /**
     * 정상적으로 회원 인증이 되었다면 JWT 생성하여 클라이언트에게 보내준다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) request.getAttribute("AuthenticetedUser");

        if(authenticatedUser != null){
            Map<String, Object> claims = new HashMap<>();
            String authenticatedUserJson = objectMapper.writeValueAsString(authenticatedUser);
            claims.put("AuthenticetedUser", authenticatedUserJson);

            Jwt jwt = jwtProvider.createJwt(claims);
            loginService.UpdateRefreshToken(authenticatedUser.getIdx(), jwt.getRefreshToken());

            String json = objectMapper.writeValueAsString(jwt);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            return false;
        }

        return true;
    }

}
