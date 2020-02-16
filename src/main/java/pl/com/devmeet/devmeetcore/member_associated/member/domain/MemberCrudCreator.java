package pl.com.devmeet.devmeetcore.member_associated.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import pl.com.devmeet.devmeetcore.domain_utils.CrudEntityCreator;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberCrudStatusEnum;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberUserNotActiveException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerAlreadyExistsException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerArgumentNotSpecified;
import pl.com.devmeet.devmeetcore.user.domain.UserEntity;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

@Builder
@NoArgsConstructor
@AllArgsConstructor
class MemberCrudCreator implements CrudEntityCreator<MemberDto, MemberEntity> {

    private MemberCrudFinder memberFinder;
    private MemberCrudSaver saver;
    private MemberUserFinder memberUserFinder;
    private MemberMessengerCreator memberMessengerCreator;

    @Override
    public MemberEntity createEntity(MemberDto dto) throws MemberAlreadyExistsException, UserNotFoundException, GroupNotFoundException, MemberNotFoundException, MessengerAlreadyExistsException, MessengerArgumentNotSpecified, MemberUserNotActiveException {
        MemberEntity memberEntity;
        UserEntity foundUser = findUser(dto);

        if (foundUser.isActive()) {
            try {
                memberEntity = memberFinder.findEntity(dto);

                if (!memberEntity.isActive())
                    return saver.saveEntity(
                            setDefaultValuesIfMemberExistButNotActive(
                                    mapToEntity(dto)));

            } catch (MemberNotFoundException e) {
                memberEntity = saver.saveEntity(
                        setDefaultValuesIfMemberNotExist(
                                connectMemberWithUser(MemberMapper.map(dto), foundUser))
                );
                createMessengerForMember(MemberMapper.map(memberEntity));

                return memberEntity;
            }
            throw new MemberAlreadyExistsException(MemberCrudStatusEnum.MEMBER_ALREADY_EXIST.toString());
        }
        throw new MemberUserNotActiveException(MemberCrudStatusEnum.MEMBER_USER_NOT_ACTIVE.toString());
    }

    private MemberEntity mapToEntity(MemberDto dto) {
        return MemberCrudService.map(dto);
    }

    private MemberEntity setDefaultValuesIfMemberNotExist(MemberEntity entity) {
        entity.setActive(true);
        entity.setCreationTime(DateTime.now());

        return entity;
    }

    private MemberEntity setDefaultValuesIfMemberExistButNotActive(MemberEntity entity) {
        entity.setActive(true);
        entity.setModificationTime(DateTime.now());

        return entity;
    }

    private MemberEntity connectMemberWithUser(MemberEntity memberEntity, UserEntity userEntity) throws UserNotFoundException {
        return new MemberUserConnector().connect(memberEntity, userEntity);
    }

    private UserEntity findUser(MemberDto memberDto) throws UserNotFoundException {
        return memberUserFinder.findUserEntity(memberDto.getUser());
    }

    private void createMessengerForMember(MemberDto memberDto) throws UserNotFoundException, MemberNotFoundException, MessengerAlreadyExistsException, GroupNotFoundException, MessengerArgumentNotSpecified {
        memberMessengerCreator.createMessenger(memberDto);
    }
}