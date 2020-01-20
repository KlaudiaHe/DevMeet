package pl.com.devmeet.devmeetcore.member_associated.place.domain;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PlaceDtoApi {

    private String userEmail;
    private String placeName;
}
