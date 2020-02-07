package pl.com.devmeet.devmeetcore.user.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.user.domain.UserDto;
import pl.com.devmeet.devmeetcore.user.domain.UserService;


@RestController
@RequestMapping(value = "/register-user")
class UserRegistration {

    private UserService service;

    private MemberCrudService memberService;

    @Autowired
    UserRegistration(UserService service) {
        this.service = service;
    }

    @PostMapping(value = "/{email}")
    ResponseEntity<String> register(@PathVariable String email) {
        UserDto user = UserDto.builder()
                .email(email)
                .build();
        return new ResponseEntity<>(service.add(user).toString(), HttpStatus.OK);
    }


}
