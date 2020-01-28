package pl.com.devmeet.devmeetcore.member_associated.member.domain;

import pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain.MessengerCrudService;
import pl.com.devmeet.devmeetcore.user.domain.UserCrudService;

import java.util.List;
import java.util.stream.Collectors;

class MemberMapper {

    static MemberEntity map(MemberDto dto) {
        return dto != null ? new MemberEntity().builder()
                .user(UserCrudService.map(dto.getUser()))
                .nick(dto.getNick())
//                      .groups(dto.getGroups())
//                      .availabilities(dto.getAvailabilities())
//                      .places(dto.getPlaces())
                .messenger(MessengerCrudService.map(dto.getMessenger()))
                .creationTime(dto.getCreationTime())
                .modificationTime(dto.getModificationTime())
                .isActive(dto.isActive())
                .build() : null;
    }

    static MemberDto map(MemberEntity memberEntity) {
        return memberEntity != null ? new MemberDto().builder()
                .user(UserCrudService.map(memberEntity.getUser()))
                .nick(memberEntity.getNick())
                //      .groups(memberEntity.getGroups())
                //      .availabilities(memberEntity.getAvailabilities())
                //      .places(memberEntity.getPlaces())
                .messenger(MessengerCrudService.map(memberEntity.getMessenger()))
                .creationTime(memberEntity.getCreationTime())
                .modificationTime(memberEntity.getModificationTime())
                .isActive(memberEntity.isActive())
                .build() : null;
    }

    static List<MemberEntity> mapDtoList(List<MemberDto> memberDtoList) {
        return memberDtoList.stream()
                .map(MemberMapper::map)
                .collect(Collectors.toList());
    }

    static List<MemberDto> mapEntityList(List<MemberEntity> memberEntityList) {
        return memberEntityList.stream()
                .map(MemberMapper::map)
                .collect(Collectors.toList());
    }
}