package project.booker.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import project.booker.controller.LoginController.dto.request.LoginRequestDto;
import project.booker.domain.Member;
import project.booker.dto.AuthenticatedUser;
import project.booker.service.loginService.LoginService;

import java.util.HashMap;
import java.util.Map;

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
                LoginRequestDto loginRequestDto = objectMapper.readValue(request.getReader(), LoginRequestDto.class);
                Member VerifyUser = loginService.VerifyUser(loginRequestDto);
                if (VerifyUser != null) {
                    request.setAttribute("AuthenticetedUser", new AuthenticatedUser(VerifyUser.getId(), VerifyUser.getName()));
                } else {
                    throw new IllegalAccessException();
                }
            } catch(Exception e){
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
