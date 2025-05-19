package eduPanel.advice;

import eduPanel.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public void handleAppWideExceptions(AppException exp) {
        ResponseStatusException resExp;
        if (exp.getErrorCode() == 404) {
            resExp = new ResponseStatusException(HttpStatus.NOT_FOUND,
                    exp.getMessage());
        } else {
            resExp = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    exp.getMessage());
        }
        exp.initCause(resExp);
        throw resExp;
    }
}