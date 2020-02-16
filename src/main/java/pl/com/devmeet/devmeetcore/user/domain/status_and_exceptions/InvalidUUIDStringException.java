package pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions;

public class InvalidUUIDStringException extends IllegalArgumentException {
    public InvalidUUIDStringException(String message) {
        super(message);
    }
}
