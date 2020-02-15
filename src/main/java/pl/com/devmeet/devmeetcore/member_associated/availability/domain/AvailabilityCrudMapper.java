package pl.com.devmeet.devmeetcore.member_associated.availability.domain;

import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;

import java.util.List;
import java.util.stream.Collectors;

class AvailabilityCrudMapper {

    public static AvailabilityDto map(AvailabilityEntity entity) {
        return entity != null ? AvailabilityDto.builder()
                .id(entity.getId())
                .member(MemberCrudService.map(entity.getMember()))
                .beginDateTime(entity.getBeginTime())
                .endDateTime(entity.getEndTime())
                .remoteWork(entity.isRemoteWork())
//                .availabilityVote(entity.getAvailabilityVote())
                .creationTime(entity.getCreationTime())
                .modificationTime(entity.getModificationTime())
                .isActive(entity.isActive())
                .build() : null;
    }

    public static AvailabilityEntity map(AvailabilityDto dto) {
        return dto != null ? AvailabilityEntity.builder()
                .Id(dto.getId())
                .member(MemberCrudService.map(dto.getMember()))
                .beginTime(dto.getBeginDateTime())
                .endTime(dto.getEndDateTime())
                .remoteWork(dto.isRemoteWork())
//                .availabilityVote(dto.getAvailabilityVote())
                .creationTime(dto.getCreationTime())
                .modificationTime(dto.getModificationTime())
                .isActive(dto.isActive())
                .build() : null;
    }

    public static List<AvailabilityDto> mapDtoList(List<AvailabilityEntity> entities) {
        return entities.stream()
                .map(entity -> map(entity))
                .collect(Collectors.toList());
    }

    public static List<AvailabilityEntity> mapEntityList(List<AvailabilityDto> dtos) {
        return dtos.stream()
                .map(dto -> map(dto))
                .collect(Collectors.toList());
    }
}

