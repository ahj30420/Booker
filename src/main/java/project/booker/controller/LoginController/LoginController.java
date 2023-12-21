package project.booker.controller.LoginController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.booker.controller.LoginController.dto.request.JoinRequestDto;
import project.booker.controller.LoginController.dto.request.ReTokenRequestDto;
import project.booker.exception.exceptions.ValidationException;
import project.booker.service.loginService.LoginService;
import project.booker.util.jwt.Jwt;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final MessageSource messageSource;

    /**
     * 일반 회원가입
     */
    @PostMapping("/signup")
    public String join(@Validated @ModelAttribute JoinRequestDto joinRequestDto, BindingResult bindingResult){

        //회원가입 시 입력값 검증
        if(bindingResult.hasErrors()){
            sendValidationError(bindingResult);
        }

        loginService.NomarlJoin(joinRequestDto);

        return "Join success";
    }

    /**
     * refresh 토큰으로 토큰 갱신하기
     */
    @PostMapping("/auth/refresh/token")
    public ResponseEntity<Jwt> tokenRefresh(@RequestBody ReTokenRequestDto reTokenRequestDto){
        Jwt jwt = loginService.refreshToken(reTokenRequestDto.getRefreshToken());
        if(jwt == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        return ResponseEntity.ok(jwt);
    }


    //--------------------------------------Private Method-----------------------------------------------------

    /*
     * 검증 실패시 오류 메시지를 담아 Exception을 발생 시켜주는 함수
     */
    private void sendValidationError(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for(FieldError error : bindingResult.getFieldErrors()){
            String message = messageSource.getMessage(error, Locale.getDefault());
            errors.put(error.getField(),message);
        }
        throw new ValidationException(errors);
    }

}
