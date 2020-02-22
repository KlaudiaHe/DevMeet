package pl.com.devmeet.devmeetcore.member_associated.member.api;

import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.user.domain.UserDto;

class MemberApiMapper {

    public MemberDto mapToBackend(MemberApiDto apiDto){
        return apiDto != null ? MemberDto.builder()
                .id(apiDto.getId())
                .user(UserDto.builder().id(apiDto.getId()).build())
                .nick(apiDto.getNick())
                .build() : null;
    }

    public MemberApiDto mapToFrontend(MemberDto dto){
        return dto != null ? MemberApiDto.builder()
                .id(dto.getId())
                .user(dto.getUser().getId())
                .nick(dto.getNick())
                .build() : null;
    }
}
