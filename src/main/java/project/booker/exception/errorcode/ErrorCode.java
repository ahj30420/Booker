package project.booker.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    DUPLICATED_USER_ID(BAD_REQUEST, "이미 사용 중인 아이디입니다."),
    INVALID_RefreshToken(UNAUTHORIZED,"올바르지 않은 Token입니다. 다시 로그인 해주세요.");

    private HttpStatus status;
    private String message;

}
