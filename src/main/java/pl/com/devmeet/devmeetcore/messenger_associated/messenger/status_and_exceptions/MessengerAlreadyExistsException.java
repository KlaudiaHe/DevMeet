package pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions;

import pl.com.devmeet.devmeetcore.domain_utils.exceptions.CrudException;

/**
 * Created by IntelliJ IDEA.
 * User: kamil
 * Date: 29.11.2019
 * Time: 18:52
 */

public class MessengerAlreadyExistsException extends CrudException {
    public MessengerAlreadyExistsException(String message) {
        super(message);
    }
}
