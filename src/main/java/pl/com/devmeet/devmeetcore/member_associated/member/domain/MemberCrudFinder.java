package pl.com.devmeet.devmeetcore.member_associated.member.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.com.devmeet.devmeetcore.domain_utils.CrudEntityFinder;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberCrudStatusEnum;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.UserDto;
import pl.com.devmeet.devmeetcore.user.domain.UserEntity;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
class MemberCrudFinder implements CrudEntityFinder<MemberDto, MemberEntity> {

    @NonNull
    private MemberRepository memberRepository;
    @NonNull
    private MemberUserFinder userFinder;

    @Override
    public MemberEntity findEntity(MemberDto dto) throws MemberNotFoundException, UserNotFoundException {
        if (dto.getId() != null)
            return findById(dto.getId());
        else
            return findMemberEntityByUser(dto.getUser());

    }

//    public MemberEntity findEntityByUser(MemberDto dto) throws MemberNotFoundException, UserNotFoundException {
//        UserDto userDto;
//
//        try {
//            userDto = dto.getUser();
//        } catch (NullPointerException e) {
//            throw new MemberNotFoundException(MemberCrudStatusEnum.MEMBER_NOT_FOUND.toString());
//        }
//
//        return findMember(userDto);
//    }

    public MemberEntity findMemberEntityByUser(UserDto userDto) throws MemberNotFoundException, UserNotFoundException {
        UserEntity userEntity = findUser(userDto);
        return memberRepository
                .findByUser(userEntity)
                .orElseThrow(() -> new MemberNotFoundException(MemberCrudStatusEnum.MEMBER_NOT_FOUND.toString()));
    }

    private UserEntity findUser(UserDto dto) throws UserNotFoundException {
        return userFinder.findUserEntity(dto);
    }

    @Override
    public List<MemberEntity> findEntities(MemberDto dto) {
        return null;
    }

    @Override
    public boolean isExist(MemberDto dto) {
        try {
            findMemberEntityByUser(dto.getUser());
            return true;
        } catch (UserNotFoundException | MemberNotFoundException | NullPointerException e) {
            return false;
        }
    }

    public MemberEntity findById(Long id) throws MemberNotFoundException {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(MemberCrudStatusEnum.MEMBER_NOT_FOUND.toString()));
    }
}
