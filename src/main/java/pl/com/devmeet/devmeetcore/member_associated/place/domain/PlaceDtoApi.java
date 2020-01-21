package pl.com.devmeet.devmeetcore.member_associated.place.domain;

import lombok.*;
import org.joda.time.DateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PlaceDtoApi {

    private Long id;
    private String userEmail;
    private String placeName;
    private String description;
    private String website;
    private String location;
    private DateTime creationTime;
    private DateTime modificationTime;
    private boolean isActive;

}
