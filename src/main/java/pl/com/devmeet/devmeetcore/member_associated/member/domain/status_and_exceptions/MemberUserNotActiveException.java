package pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions;

import pl.com.devmeet.devmeetcore.domain_utils.exceptions.CrudException;

public class MemberUserNotActiveException extends CrudException {
    public MemberUserNotActiveException(String message) {
        super(message);
    }
}
