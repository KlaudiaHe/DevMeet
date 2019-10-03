package pl.com.devmeet.devmeet.member_associated.place.domain;

import lombok.*;
import org.joda.time.DateTime;
import pl.com.devmeet.devmeet.member_associated.availability.domain.AvailabilityDto;
import pl.com.devmeet.devmeet.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeet.poll_associated.poll.domain.PollDto;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PlaceDto {

    private MemberDto member;

    private String placeName;
    private String description;
    private String website;
    private String location;

    private AvailabilityDto availability;

    private PollDto poll;

    private DateTime creationTime;
    private boolean isActive;
}
