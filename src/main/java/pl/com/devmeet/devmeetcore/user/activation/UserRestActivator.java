package pl.com.devmeet.devmeetcore.user.activation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberUserNotActiveException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerAlreadyExistsException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerArgumentNotSpecified;
import pl.com.devmeet.devmeetcore.user.domain.UserService;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.InvalidUUIDStringException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserAlreadyActiveException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/user-activate")
class UserRestActivator {

    private UserService userService;

    @Autowired
    UserRestActivator(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{email}/{userKey}")
    ResponseEntity<String> activate(@Valid @PathVariable String email,
                                    @PathVariable String userKey) throws UserNotFoundException, UserAlreadyActiveException, InvalidUUIDStringException, GroupNotFoundException, MessengerArgumentNotSpecified, MemberAlreadyExistsException, MessengerAlreadyExistsException, MemberNotFoundException, MemberUserNotActiveException {
        return new ResponseEntity<>(userService.activateUser(email, userKey), HttpStatus.OK);
    }
}
