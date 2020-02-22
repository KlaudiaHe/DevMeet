package pl.com.devmeet.devmeetcore.member_associated.member.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/member")
public class MemberGetterApi {

    private MemberCrudService memberService;
    private MemberApiMapper apiMapper;

    @Autowired
    private MemberGetterApi(MemberCrudService memberService) {
        this.memberService = memberService;
    }

    @GetMapping({"userId"})
    public ResponseEntity<MemberApiDto> getSignInMember(@PathVariable Long userId) {
        try {
            return new ResponseEntity<MemberApiDto>(
                    apiMapper.mapToFrontend(memberService.findByUserId(userId)),
                    HttpStatus.FOUND);
        } catch (MemberNotFoundException | UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Resource with user id(" + userId + ") not found because" + e.getMessage());
        }
    }
}
