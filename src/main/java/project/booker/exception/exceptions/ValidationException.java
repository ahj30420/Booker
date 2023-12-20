package project.booker.exception.exceptions;

import java.util.Map;

public class ValidationException extends RuntimeException{

    Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors(){
        return errors;
    }

}
