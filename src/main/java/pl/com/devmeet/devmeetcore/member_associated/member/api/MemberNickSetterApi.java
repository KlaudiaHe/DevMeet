package pl.com.devmeet.devmeetcore.member_associated.member.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberFoundButNotActiveException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;

@RestController
@RequestMapping("/api/v1/member")
public class MemberNickSetterApi {

    private MemberCrudService memberService;
    private MemberApiMapper apiMapper;

    @Autowired
    private MemberNickSetterApi(MemberCrudService memberService) {
        this.memberService = memberService;
    }

    @PutMapping
    public ResponseEntity<MemberApiDto> updateMemberByMemberId(@RequestBody MemberApiDto member) {
        try {
            return ResponseEntity
                    .accepted()
                    .body(apiMapper.mapToFrontend(
                            memberService.update(
                                    apiMapper.mapToBackend(member)))
                    );

        } catch (MemberNotFoundException | MemberFoundButNotActiveException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Can't update member. Error: " + e.getMessage());
        }
    }
}
