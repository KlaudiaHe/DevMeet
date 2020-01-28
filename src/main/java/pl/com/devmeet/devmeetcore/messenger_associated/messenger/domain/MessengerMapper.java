package pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain;

import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;

class MessengerMapper {

    static MessengerDto map(MessengerEntity messengerEntity) {
        return messengerEntity != null ? MessengerDto.builder()
                .member(MemberCrudService.map(messengerEntity.getMember()))
                .group(GroupCrudService.map(messengerEntity.getGroup()))
                .creationTime(messengerEntity.getCreationTime())
                .isActive(messengerEntity.isActive())
                .build() : null;

    }

    static MessengerEntity map(MessengerDto messengerDto) {
        return messengerDto != null ? MessengerEntity.builder()
                .member(MemberCrudService.map(messengerDto.getMember()))
                .group(GroupCrudService.map(messengerDto.getGroup()))
                .creationTime(messengerDto.getCreationTime())
                .isActive(messengerDto.isActive())
                .build() : null;
    }
}
