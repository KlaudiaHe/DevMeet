package pl.com.devmeet.devmeetcore.member_associated.member.domain;

import lombok.*;
import org.joda.time.DateTime;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberCrudStatusEnum;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberFoundButNotActiveException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;
import sun.font.DelegatingShape;

@AllArgsConstructor
@NoArgsConstructor
@Builder
class MemberCrudUpdater {

    private MemberCrudFinder memberFinder;
    private MemberCrudSaver memberSaver;

    public MemberEntity update(MemberDto member) throws MemberNotFoundException, MemberFoundButNotActiveException {
        MemberEntity foundMember = findMemberById(member.getId());

        if (foundMember.isActive())
            return memberSaver.saveEntity(updateAllowedValues(foundMember, member));

        throw new MemberFoundButNotActiveException(MemberCrudStatusEnum.MEMBER_FOUND_BUT_NOT_ACTIVE.toString());
    }

//    public MemberEntity update(MemberDto oldDto, MemberDto newDto) throws MemberFoundButNotActiveException, MemberNotFoundException, UserNotFoundException {
//        MemberEntity foundMember = findMember(oldDto);
//
//        if (foundMember.isActive())
//            return memberSaver.saveEntity(updateAllowedValues(foundMember, newDto));
//
//        throw new MemberFoundButNotActiveException(MemberCrudStatusEnum.MEMBER_FOUND_BUT_NOT_ACTIVE.toString());
//    }
//
//    private MemberEntity findMember(MemberDto dto) throws MemberNotFoundException, UserNotFoundException {
//        return memberFinder.findEntity(dto);
//    }

    private MemberEntity findMemberById(Long id) throws MemberNotFoundException {
        if (id != null)
            return memberFinder.findById(id);
        else
            throw new MemberNotFoundException(MemberCrudStatusEnum.ID_NOT_SPECIFIED.toString());
    }

    private MemberEntity updateAllowedValues(MemberEntity oldMember, MemberDto updatedMember) {
        String nick = updatedMember.getNick();

        if (nick != null) {
            oldMember.setNick(nick);
            oldMember.setModificationTime(DateTime.now());
        }

        return oldMember;
    }
}
