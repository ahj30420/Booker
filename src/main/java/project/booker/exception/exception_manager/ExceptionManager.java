package project.booker.exception.exception_manager;

import ch.qos.logback.core.spi.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.booker.exception.exceptions.*;

import java.util.Map;

@RestControllerAdvice
public class ExceptionManager {

    /**
     * Exception 관리
     * Error 메세지와 HttpStatus를 Response에 담아 client에게 보내줍니다.
     */

    //ValidationException 발생 시 작동(입력값 검증 예외 처리)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String,String>> ValidationExceptionHandler(ValidationException e){
        return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
    }

    //JoinException 발생 시 작동(아이디 중복 예외 처리)
    @ExceptionHandler(DuplicatedIDException.class)
    public ResponseEntity<Response> DuplicatedIDException(DuplicatedIDException e){
        return new ResponseEntity(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()), e.getErrorCode().getStatus());
    }


    //--------------------------------------Inner Class-----------------------------------------------------
    
    /**
     * ResponseEntity의 Body를 JSON 형식으로 보내기 위한 Inner Class
     */
    @AllArgsConstructor
    @Getter
    public static class Response{
        private String code;
        private String message;

        public static Response error(String code, String message){
            return new Response(code, message);
        }
    }

}
