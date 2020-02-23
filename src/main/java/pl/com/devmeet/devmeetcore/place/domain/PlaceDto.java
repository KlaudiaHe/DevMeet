package pl.com.devmeet.devmeetcore.place.domain;

import lombok.*;
import org.joda.time.DateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PlaceDto {

    private Long id;
//    private List<MemberDto> members;

    private String placeName;
    private String description;
    private String website;
    private String location;

    //   private AvailabilityDto availability;

//    private List<PlaceVoteEntity> placeVotes;

    private DateTime creationTime;
    private DateTime modificationTime;
    private boolean isActive;
}
