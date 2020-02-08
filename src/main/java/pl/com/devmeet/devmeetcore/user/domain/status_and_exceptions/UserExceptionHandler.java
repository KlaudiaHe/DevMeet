package pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserExceptionHandler {

    @ResponseBody
    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundHandler(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({UserAlreadyActiveException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String userAlreadyActiveHandler(UserAlreadyActiveException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({UserAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String userAlreadyExistsHandler(UserAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({InvalidUUIDStringException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String invalidUUIDStringHandler(InvalidUUIDStringException ex) {
        return ex.getMessage();
    }

}
