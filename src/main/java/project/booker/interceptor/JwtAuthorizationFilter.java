package project.booker.interceptor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.servlet.HandlerInterceptor;
import project.booker.dto.AuthenticatedUser;
import project.booker.util.jwt.JwtProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!ContainToken(request)){
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "인증 오류");
            return false;
        }

        try{
            String token = getToken(request);
            AuthenticatedUser authenticatedUser = getAuthenticatedUser(token);
            log.info("User ID={}, NAME={}", authenticatedUser.getId(), authenticatedUser.getName());
        } catch (JsonParseException e){
            log.error("JsonParseException");
            setErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "입력 오류");
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e){
            log.error("JwtException");
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "인증 오류");
        } catch (ExpiredJwtException e){
            log.error("JwtTokenExpired");
            setErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "토큰이 만료 되었습니다.");
        } catch (AuthorizationServiceException e){
            log.error("AuthorizationException");
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "권한이 없습니다.");
        }
        return true;
    }

    private void setErrorResponse(HttpServletResponse response, int Status, String message) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(Status);

        Map<String, String> ErrorMessage = new HashMap<>();
        ErrorMessage.put("message", message);
        String json = objectMapper.writeValueAsString(ErrorMessage);
        response.getWriter().write(json);
    }

    private boolean ContainToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return authorization != null && authorization.startsWith("Bearer ");
    }


    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return authorization.substring(7);
    }


    private AuthenticatedUser getAuthenticatedUser(String token) throws JsonProcessingException {
        Claims claims = jwtProvider.getClaims(token);
        String authenticatedUserJson = claims.get("AuthenticetedUser", String.class);
        return objectMapper.readValue(authenticatedUserJson, AuthenticatedUser.class);
    }
}
