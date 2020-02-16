package pl.com.devmeet.devmeetcore.member_associated.member.domain;

import lombok.*;
import org.joda.time.DateTime;
import pl.com.devmeet.devmeetcore.user.domain.UserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MemberDto {

    private Long id;
    private UserDto user;
    private String nick;

//    private MessengerDto messenger;

//    private List<GroupDto> groups;

//    private List<AvailabilityDto> availabilities;
//    private List<PlaceDto> places;

    private DateTime creationTime;
    private DateTime modificationTime;

    private boolean isActive;
}
