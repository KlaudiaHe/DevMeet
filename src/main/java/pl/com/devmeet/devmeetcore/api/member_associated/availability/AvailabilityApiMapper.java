package pl.com.devmeet.devmeetcore.api.member_associated.availability;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.AvailabilityDto;

import java.util.List;
import java.util.stream.Collectors;

class AvailabilityApiMapper {

    public AvailabilityDto mapToBackend(AvailabilityApiDto apiDto) {
        String parserDateTimeFormatForDto = "yyyy/MM/dd HH:mm";

        return apiDto != null ? AvailabilityDto.builder()
                .id(apiDto.getId())
                .beginDateTime(DateTime.parse((apiDto.getBeginDate() + " " + apiDto.getBeginTime()), DateTimeFormat.forPattern(parserDateTimeFormatForDto)))
                .endDateTime(DateTime.parse((apiDto.getEndDate() + " " + apiDto.getEndTime()), DateTimeFormat.forPattern(parserDateTimeFormatForDto)))
                .freeTime(apiDto.isFreeTime())
                .remoteWork(apiDto.isRemoteWork())
                .build() : null;
    }

    public List<AvailabilityDto> mapListToBackend(List<AvailabilityApiDto> apiDtos){
        return apiDtos.parallelStream()
                .map(this::mapToBackend)
                .collect(Collectors.toList());
    }

    public AvailabilityApiDto mapToFrontend(AvailabilityDto dto) {
        String printerTimeFormatForApiDto = "HH:mm";
        String printerDateFormatForApiDto = "yyyy/MM/dd";

        return dto != null ? AvailabilityApiDto.builder()
                .id(dto.getId())
                .beginDate(dto.getBeginDateTime().toString(printerDateFormatForApiDto))
                .beginTime(dto.getBeginDateTime().toString(printerTimeFormatForApiDto))
                .endDate(dto.getEndDateTime().toString(printerDateFormatForApiDto))
                .endTime(dto.getEndDateTime().toString(printerTimeFormatForApiDto))
                .freeTime(dto.isFreeTime())
                .remoteWork(dto.isRemoteWork())
                .build() : null;
    }

    public List<AvailabilityApiDto> mapListToFrontend(List<AvailabilityDto> dtos){
        return dtos.parallelStream()
                .map(this::mapToFrontend)
                .collect(Collectors.toList());
    }
}
