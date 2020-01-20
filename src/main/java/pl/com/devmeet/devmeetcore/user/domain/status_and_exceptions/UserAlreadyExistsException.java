package pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions;

import pl.com.devmeet.devmeetcore.domain_utils.exceptions.CrudException;

public class UserAlreadyExistsException extends CrudException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}