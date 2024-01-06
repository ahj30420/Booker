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

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class VerifyUserFilter implements HandlerInterceptor {

    private final LoginService loginService;
    private final ObjectMapper objectMapper;

    /**
     * 로그인 정보로 회원 인증
     * 1. 입력받은 ID,PW로 회원 검증
     * 2. JWT 발급을 위해 인증된 회원 정보를 request에 담아준다.
     * 3. 검증 실패시 재로그인 요청(login 페이지로 이동)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(request.getMethod().equals("POST")){
            try {
                LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);
                Member VerifyUser = loginService.VerifyUser(loginDto);
                if (VerifyUser != null) {
                    request.setAttribute("AuthenticetedUser", new AuthenticatedUser(VerifyUser.getMemberProfile().getProfileIdx(), VerifyUser.getName(), VerifyUser.getMemberProfile().getNickname()));
                    request.setAttribute("MemberIdx", VerifyUser.getMemberIdx());
                } else {
                    throw new IllegalAccessException();
                }
            } catch(Exception e){
                log.info("Exception={}",e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("Redirect","/login");

                String json = objectMapper.writeValueAsString(errorMap);

                response.getWriter().write(json);
                return false;
            }
        }

        return true;
    }
}
