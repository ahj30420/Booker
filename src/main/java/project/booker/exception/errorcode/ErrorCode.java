package project.booker.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    DUPLICATED_USER_ID(BAD_REQUEST, "이미 사용 중인 아이디 입니다."),
    INVALID_REFRESHTOKEN(UNAUTHORIZED,"올바르지 않은 Token입니다. 다시 로그인 해주세요."),
    DUPLICATED_PROFILE_NICKNAME(BAD_REQUEST, "이미 사용 중인 닉네임 입니다."),
    INVALID_CODE(BAD_REQUEST, "올바르지 않은 CODE입니다. 다시 로그인 해주세요."),
    INVALID_ACCESSTOKEN(UNAUTHORIZED,"올바르지 않은 AccessToken입니다. 다시 로그인 해주세요.");

    private HttpStatus status;
    private String message;

}
