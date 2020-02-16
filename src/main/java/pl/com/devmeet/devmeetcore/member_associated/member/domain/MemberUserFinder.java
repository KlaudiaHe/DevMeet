package pl.com.devmeet.devmeetcore.member_associated.member.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.com.devmeet.devmeetcore.user.domain.UserCrudService;
import pl.com.devmeet.devmeetcore.user.domain.UserDto;
import pl.com.devmeet.devmeetcore.user.domain.UserEntity;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserCrudStatusEnum;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: Kamil Ptasinski
 * Date: 12.11.2019
 * Time: 21:30
 */

@RequiredArgsConstructor
class MemberUserFinder {

    @NonNull
    private UserCrudService userCrudService;

    public UserEntity findUserEntity(UserDto dto) throws UserNotFoundException {
        return userCrudService.findEntity(dto);
    }
}
