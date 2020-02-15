package pl.com.devmeet.devmeetcore.member_associated.availability.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AvailabilityDto {

    private Long id;
    private MemberDto member;

    private DateTime beginDateTime;
    private DateTime endDateTime;
    private boolean remoteWork;
    private boolean freeTime;

//    private PlaceDto place;

//    private AvailabilityVoteEntity availabilityVote;

    private DateTime creationTime;
    private DateTime modificationTime;
    private boolean isActive;
}
