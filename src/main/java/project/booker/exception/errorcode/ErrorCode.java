package project.booker.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    DUPLICATED_USER_ID(HttpStatus.BAD_REQUEST, "이미 사용 중인 아이디입니다.");


    private HttpStatus status;
    private String message;

}
