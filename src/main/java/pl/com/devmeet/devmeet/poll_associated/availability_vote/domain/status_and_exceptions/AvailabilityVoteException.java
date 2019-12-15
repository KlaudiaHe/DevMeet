package pl.com.devmeet.devmeet.poll_associated.availability_vote.domain.status_and_exceptions;

import pl.com.devmeet.devmeet.domain_utils.exceptions.AppLogicException;

/**
 * Created by IntelliJ IDEA.
 * User: Kamil Ptasinski
 * Date: 16.11.2019
 * Time: 00:20
 */
public class AvailabilityVoteException extends AppLogicException {
    public AvailabilityVoteException(String message) {
        super(message);
    }
}
