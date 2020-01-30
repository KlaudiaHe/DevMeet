package pl.com.devmeet.devmeetcore.api.member_associated.availability;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
class AvailabilityDtoApi {

    private Long id;
    private Long memberId;

    private String beginTime;
    private String beginDate;

    private String endTime;
    private String endDate;


}
