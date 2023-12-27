package project.booker.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import project.booker.controller.LoginController.dto.request.LoginDto;
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
     * 인증 실패시 400 Status 반환(Redirect 주소 보내줌)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(request.getMethod().equals("POST")){
            try {
                LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);
                Member VerifyUser = loginService.VerifyUser(loginDto);
                if (VerifyUser != null) {
                    request.setAttribute("AuthenticetedUser", new AuthenticatedUser(VerifyUser.getMemberIdx(), VerifyUser.getName(), VerifyUser.getMemberProfile().getNickname()));
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
