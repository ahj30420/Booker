package project.booker.exception.exception_manager;

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

    //InvalidJwtException 발생 시 작동(올바르지 않은 RefreshToken 처리)
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<Response> InvalidRefreshTokenException(InvalidRefreshTokenException e){
        return new ResponseEntity(Response.error(e.getErrorCode().name(), e.getErrorCode().getMessage()), e.getErrorCode().getStatus());
    }

    //ValidateDuplicateNickName 발생 시 작동(프로필 닉네임 중복 예외 처리)
    @ExceptionHandler(DuplicatedNickNameException.class)
    public ResponseEntity<Response> DuplicatedNickNameException(DuplicatedNickNameException e){
        return new ResponseEntity(Response.error(e.getErrorCode().name(), e.getErrorCode().getMessage()), e.getErrorCode().getStatus());
    }

    //CodeException 발생 시 작동(OAuth2.0 올바르지 않은 Code 처리)
    @ExceptionHandler(CodeException.class)
    public ResponseEntity<Response> CodeException(CodeException e){
        return new ResponseEntity<>(Response.error(e.getErrorCode().name(), e.getErrorCode().getMessage()), e.getErrorCode().getStatus());
    }

    //InvalidAccessToken 발시 시 작동(OAuth2.0 올바르지 않은 AccessToken 처리) 
    @ExceptionHandler(InvalidAccessToken.class)
    public ResponseEntity<Response> InvalidAccessToken(InvalidAccessToken e){
        return new ResponseEntity<>(Response.error(e.getErrorCode().name(), e.getErrorCode().getMessage()), e.getErrorCode().getStatus());
    }

    //NotExistBookException 발생시 작동(개인 서재에 입력 받은 책이 없을 경우)
    @ExceptionHandler(NotExistBookException.class)
    public ResponseEntity<Response> NotExistBook(NotExistBookException e){
        return new ResponseEntity<>(Response.error(e.getErrorCode().name(), e.getErrorCode().getMessage()), e.getErrorCode().getStatus());
    }

    //InvalidProfileIdException 발생시 작동(프로필Id가 유효하지 않을 경우)
    @ExceptionHandler(InvalidProfileIdException.class)
    public ResponseEntity<Response> InvalidProfileId(InvalidProfileIdException e){
        return new ResponseEntity<>(Response.error(e.getErrorCode().name(), e.getErrorCode().getMessage()), e.getErrorCode().getStatus());
    }

    //InvalidMemberException 발생시 작동(MemberId가 유효하지 않을 경우)
    @ExceptionHandler(InvalidMemberException.class)
    public ResponseEntity<Response> InvalidMemberException(InvalidMemberException e){
        return new ResponseEntity<>(Response.error(e.getErrorCode().name(), e.getErrorCode().getMessage()), e.getErrorCode().getStatus());
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
