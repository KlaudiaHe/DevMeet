package pl.com.devmeet.devmeetcore.member_associated.member.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.devmeet.devmeetcore.user.domain.UserDto;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
class MemberApiDto {

    private Long id;
    private Long user;
    private String nick;

}
