package project.booker.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import project.booker.controller.LoginController.dto.LoginDto;
import project.booker.domain.Member;
import project.booker.dto.AuthenticatedUser;
import project.booker.service.LoginService.LoginService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.*;

@Slf4j
@RequiredArgsConstructor
public class VerifyUserFilter implements HandlerInterceptor {

    private final LoginService loginService;
    private final ObjectMapper objectMapper;

    /**
     * 로그인 정보로 회원 인증
     * 1. 입력받은 ID,PW로 회원 검증
     * 2. JWT 발급을 위해 인증된 회원 정보를 request에 담아준다.
     * <검증 실패>
     *     3-1. 프로필 정보가 없어 NullPointException이 발생했다면 프로필 등록 페이지로 이동
     *     3-2. 이외의 검증 실패시 재로그인 요청(login 페이지로 이동)
     * </>
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(request.getMethod().equals("POST")){
            String memberId = null;
            try {
                LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);
                Member verifyUser = loginService.VerifyUser(loginDto);
                if (verifyUser != null) {
                    memberId = verifyUser.getMemberId();
                    request.setAttribute("AuthenticetedUser", new AuthenticatedUser(verifyUser.getMemberProfile().getProfileId(), verifyUser.getName(), verifyUser.getMemberProfile().getNickname()));
                    request.setAttribute("MemberPk", verifyUser.getMemberPk());
                } else {
                    throw new IllegalAccessException();
                }
            } catch(NullPointerException ne){
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("memberId", memberId);
                errorMap.put("Redirect","/Profile");

                return sendExceptionAPI(response, SC_UNAUTHORIZED, errorMap);
            } catch(Exception e){
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("Redirect","/login");

                return sendExceptionAPI(response, SC_BAD_REQUEST, errorMap);
            }
        }

        return true;
    }

    //--------------------------------------Private Method-----------------------------------------------------

    private boolean sendExceptionAPI(HttpServletResponse response, int scUnauthorized, Map<String, String> errorMap) throws IOException {
        response.setStatus(scUnauthorized);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = objectMapper.writeValueAsString(errorMap);
        response.getWriter().write(json);
        return false;
    }

}
