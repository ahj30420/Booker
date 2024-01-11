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

import static jakarta.servlet.http.HttpServletResponse.*;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    /**
     * 권한에 대한 인가 처리 담당 필터
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!ContainToken(request)){
            setErrorResponse(response, SC_UNAUTHORIZED, "인증 오류");
            return false;
        }

        try{
            String token = getToken(request);
            AuthenticatedUser authenticatedUser = getAuthenticatedUser(token);
            request.setAttribute("AuthenticatedUser", authenticatedUser);
            log.info("User ID={}, NAME={}, NICKNAME={}", authenticatedUser.getProfileId(), authenticatedUser.getName(),authenticatedUser.getNickname());
            return true;
        } catch (JsonParseException e){
            log.error("JsonParseException");
            setErrorResponse(response, SC_BAD_REQUEST, "입력 오류");
            return false;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e){
            log.error("JwtException");
            setErrorResponse(response, SC_UNAUTHORIZED, "인증 오류");
            return false;
        } catch (ExpiredJwtException e){
            log.error("JwtTokenExpired");
            setErrorResponse(response, SC_FORBIDDEN, "토큰이 만료 되었습니다.");
            return false;
        } catch (AuthorizationServiceException e){
            log.error("AuthorizationException");
            setErrorResponse(response, SC_UNAUTHORIZED, "권한이 없습니다.");
            return false;
        }
    }

    //--------------------------------------Private Method-----------------------------------------------------

     /**
     * 예외 발생시 response에 Status, 오류 메시지 설정 함수
     */
    private void setErrorResponse(HttpServletResponse response, int Status, String message) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(Status);

        Map<String, String> ErrorMessage = new HashMap<>();
        ErrorMessage.put("message", message);
        String json = objectMapper.writeValueAsString(ErrorMessage);
        response.getWriter().write(json);
    }

    /**
     * Token은 일밪적으로 Header 담아 보낸다.
     * Header를 조회하여 Token을 갖고 있는 요청인지 확인(일반적으로 AccessToken임을 명시하기위해 Token 앞에 Bearer 붙여줌)
     */
    private boolean ContainToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return authorization != null && authorization.startsWith("Bearer ");
    }

    /**
     * 토큰을 조회하는 함수
     * Token 앞에 임의로 붙인 "Beare "를 제거하고 Token 값만 추출
     */

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return authorization.substring(7);
    }

    /**
     * Token의 Claims 값을 가져와 AuthenticatedUser 객체로 매핑하는 함수
     */
    private AuthenticatedUser getAuthenticatedUser(String token) throws JsonProcessingException {
        Claims claims = jwtProvider.getClaims(token);
        Map<String, Object> authenticatedUserClaim = (Map<String, Object>) claims.get("AuthenticetedUser");

        String pofileId = (String) authenticatedUserClaim.get("profileId");
        String name = (String) authenticatedUserClaim.get("name");
        String nickname = (String) authenticatedUserClaim.get("nickname");

        return new AuthenticatedUser(pofileId, name, nickname);
    }
}
