package pl.com.devmeet.devmeetcore.poll_associated.availability_vote.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberEntity;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailabilityVoteMemberFinder {

    private MemberCrudService memberCrudService;

    public MemberEntity findMember(MemberDto dto) throws MemberNotFoundException, UserNotFoundException {
            return memberCrudService.findEntity(dto);
    }
}
