package pl.com.devmeet.devmeetcore.member_associated.availability.api;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
class AvailabilityApiDto {

    private Long id;
    private Long memberId;

    private String beginDate;
    private String beginTime;

    private String endDate;
    private String endTime;

    private boolean freeTime;
    private boolean remoteWork;
}
