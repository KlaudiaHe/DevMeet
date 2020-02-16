package pl.com.devmeet.devmeetcore.member_associated.availability.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.com.devmeet.devmeetcore.domain_utils.CrudEntityUpdater;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityCrudInfoStatusEnum;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityException;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

@AllArgsConstructor
@NoArgsConstructor
@Builder
class AvailabilityCrudUpdater implements CrudEntityUpdater<AvailabilityDto, AvailabilityEntity> {

    private AvailabilityCrudSaver availabilityCrudSaver;
    private AvailabilityCrudFinder availabilityCrudFinder;

    public AvailabilityEntity updateEntityById(AvailabilityDto newDto) throws UserNotFoundException, AvailabilityNotFoundException, AvailabilityException, MemberNotFoundException {
        AvailabilityDto dummyAvailability = AvailabilityDto.builder()
                .id(newDto.getId())
                .member(newDto.getMember())
                .build();
        return updateEntity(dummyAvailability, newDto);
    }

    @Override
    public AvailabilityEntity updateEntity(AvailabilityDto oldDto, AvailabilityDto newDto) throws MemberNotFoundException, UserNotFoundException, AvailabilityException, AvailabilityNotFoundException {
        AvailabilityEntity oldAvailability = findAvailabilityEntity(oldDto);
        AvailabilityEntity newAvailability = mapDtoToEntity(checkMember(oldDto, newDto));

        return availabilityCrudSaver.saveEntity(updateAllowedParameters(oldAvailability, newAvailability));
    }

    AvailabilityEntity findAvailabilityEntity(AvailabilityDto oldDto) throws MemberNotFoundException, AvailabilityNotFoundException, UserNotFoundException {
        return availabilityCrudFinder.findEntityByIdOrByMember(oldDto);
    }

    private AvailabilityDto checkMember(AvailabilityDto oldDto, AvailabilityDto newDto) throws AvailabilityException {
        if (oldDto.getMember().getNick().equals(newDto.getMember().getNick()))
            return newDto;
        throw new AvailabilityException(AvailabilityCrudInfoStatusEnum.AVAILABILITY_NOT_FOUND.toString());
    }

    private AvailabilityEntity mapDtoToEntity(AvailabilityDto dto) {
        return AvailabilityCrudService.map(dto);
    }

    private AvailabilityEntity updateAllowedParameters(AvailabilityEntity oldEntity, AvailabilityEntity newEntity) {
        oldEntity.setBeginTime(newEntity.getBeginTime());
        oldEntity.setEndTime(newEntity.getEndTime());
        oldEntity.setRemoteWork(newEntity.isRemoteWork());
//        oldEntity.setModificationTime(DateTime.now());
        return oldEntity;
    }
}
