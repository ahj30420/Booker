package project.booker.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.booker.exception.errorcode.ErrorCode;

@Getter
@AllArgsConstructor
public class NotExistBookException extends RuntimeException{
    private ErrorCode errorCode;
}
