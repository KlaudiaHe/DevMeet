package pl.com.devmeet.devmeetcore.user.activation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
                                    @PathVariable String userKey) throws UserNotFoundException, UserAlreadyActiveException, InvalidUUIDStringException {
        return new ResponseEntity<>(userService.activateUser(email, userKey), HttpStatus.OK);
    }
}
