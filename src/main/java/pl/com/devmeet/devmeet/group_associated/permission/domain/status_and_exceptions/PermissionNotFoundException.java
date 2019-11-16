package pl.com.devmeet.devmeet.group_associated.permission.domain.status_and_exceptions;

import pl.com.devmeet.devmeet.domain_utils.exceptions.CrudException;

/**
 * Created by IntelliJ IDEA.
 * User: Kamil Ptasinski
 * Date: 16.11.2019
 * Time: 11:31
 */
public class PermissionNotFoundException extends CrudException {
    public PermissionNotFoundException(String message) {
        super(message);
    }
}
