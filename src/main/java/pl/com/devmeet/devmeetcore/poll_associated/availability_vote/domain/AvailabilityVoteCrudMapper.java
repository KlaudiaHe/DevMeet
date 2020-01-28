package pl.com.devmeet.devmeetcore.poll_associated.availability_vote.domain;

import pl.com.devmeet.devmeetcore.member_associated.availability.domain.AvailabilityCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.poll_associated.poll.domain.PollCrudService;

import java.util.List;
import java.util.stream.Collectors;

class AvailabilityVoteCrudMapper {

    public static AvailabilityVoteEntity map (AvailabilityVoteDto dto){
        return dto != null ? AvailabilityVoteEntity.builder()
                .poll(PollCrudService.map(dto.getPoll()))
                .availability(AvailabilityCrudService.map(dto.getAvailability()))
                .member(MemberCrudService.map(dto.getMember()))
                .creationTime(dto.getCreationTime())
                .isActive(dto.isActive())
                .build() : null;
    }

    public static AvailabilityVoteDto map (AvailabilityVoteEntity entity){
        return entity != null ? AvailabilityVoteDto.builder()
                .poll(PollCrudService.map(entity.getPoll()))
                .availability(AvailabilityCrudService.map(entity.getAvailability()))
                .member(MemberCrudService.map(entity.getMember()))
                .creationTime(entity.getCreationTime())
                .isActive(entity.isActive())
                .build() : null;
    }

    public static List<AvailabilityVoteEntity> mapToEntities(List<AvailabilityVoteDto> dtos){
        return dtos.stream()
                .map(dto -> map(dto))
                .collect(Collectors.toList());
    }

    public static List<AvailabilityVoteDto> mapToDtos(List<AvailabilityVoteEntity> entities){
        return entities.stream()
                .map(AvailabilityVoteCrudMapper::map)
                .collect(Collectors.toList());
    }
 }
