package pl.com.devmeet.devmeetcore.member_associated.member.domain;

import pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain.MessengerCrudService;
import pl.com.devmeet.devmeetcore.user.domain.UserCrudService;

import java.util.List;
import java.util.stream.Collectors;

class MemberMapper {

    static MemberEntity map(MemberDto dto) {
        return dto != null ? MemberEntity.builder()
                .Id(dto.getId())
                .user(UserCrudService.map(dto.getUser()))
                .nick(dto.getNick())
                .messenger(MessengerCrudService.map(dto.getMessenger()))
                .creationTime(dto.getCreationTime())
                .modificationTime(dto.getModificationTime())
                .isActive(dto.isActive())
                .build() : null;
    }

    static MemberDto map(MemberEntity memberEntity) {
        return memberEntity != null ? MemberDto.builder()
                .id(memberEntity.getId())
                .user(UserCrudService.map(memberEntity.getUser()))
                .nick(memberEntity.getNick())
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