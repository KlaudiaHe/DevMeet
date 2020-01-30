package pl.com.devmeet.devmeetcore.poll_associated.poll.domain;

import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudService;

import java.util.List;
import java.util.stream.Collectors;

class PollCrudMapper {

    public static PollDto map(PollEntity entity) {
        return entity != null ? PollDto.builder()
                .id(entity.getId())
                .group(GroupCrudService.map(entity.getGroup()))
                .creationTime(entity.getCreationTime())
                .active(entity.isActive())
                .build() : null;
    }

    public static PollEntity map(PollDto dto) {
        return dto != null ? PollEntity.builder()
                .Id(dto.getId())
                .group(GroupCrudService.map(dto.getGroup()))
                .creationTime(dto.getCreationTime())
                .active(dto.isActive())
                .build() : null;
    }

    public static List<PollDto> mapToDtos(List<PollEntity> entities) {
        return entities.stream()
                .map(entity -> map(entity))
                .collect(Collectors.toList());
    }

    public static List<PollEntity> mapToEntities(List<PollDto> dtos) {
        return dtos.stream()
                .map(dto -> map(dto))
                .collect(Collectors.toList());
    }
}
