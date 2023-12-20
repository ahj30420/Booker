package project.booker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import project.booker.dto.joinController.request.JoinRequestDto;
import project.booker.exception.exceptions.ValidationException;
import project.booker.service.joinService.JoinService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;
    private final MessageSource messageSource;

    /*
     * 일반 회원가입
     */
    @PostMapping("/signup")
    public String join(@Validated @ModelAttribute JoinRequestDto joinRequestDto, BindingResult bindingResult){

        //회원가입 시 입력값 검증
        if(bindingResult.hasErrors()){
            sendValidationError(bindingResult);
        }

        joinService.NomarlJoin(joinRequestDto);

        return "Join success";
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
