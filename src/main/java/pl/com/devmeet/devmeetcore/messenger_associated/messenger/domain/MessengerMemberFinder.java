package pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberEntity;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: Kamil Ptasinski
 * Date: 29.11.2019
 * Time: 23:07
 */

@RequiredArgsConstructor
class MessengerMemberFinder {

    @NonNull
    private MemberCrudService memberCrudService;

    public MemberEntity findMember(MemberDto memberDto) throws MemberNotFoundException, UserNotFoundException {
        return memberCrudService.findEntity(memberDto);
    }
}
