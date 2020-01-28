package pl.com.devmeet.devmeetcore.member_associated.member.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain.MessengerCrudService;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain.MessengerDto;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerAlreadyExistsException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

@RequiredArgsConstructor
class MemberMessengerDeactivator {

    @NonNull
    private MessengerCrudService messengerCrudService;

    public MessengerDto deactivateMessenger(MemberEntity memberEntity) throws UserNotFoundException, GroupNotFoundException, MessengerAlreadyExistsException, MessengerNotFoundException, MemberNotFoundException {
        return messengerCrudService.deactivateMembersMessenger(MemberCrudService.map(memberEntity));
    }
}
