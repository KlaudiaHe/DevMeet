package pl.com.devmeet.devmeetcore.poll_associated.poll.domain.status_and_exceptions;

import pl.com.devmeet.devmeetcore.domain_utils.exceptions.CrudException;

/**
 * Created by IntelliJ IDEA.
 * User: Kamil Ptasinski
 * Date: 16.11.2019
 * Time: 09:11
 */
public class PollNotFoundException extends CrudException {
    public PollNotFoundException(String message) {
        super(message);
    }
}
