package pl.com.devmeet.devmeetcore.member_associated.availability.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.com.devmeet.devmeetcore.domain_utils.CrudEntityFinder;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityCrudInfoStatusEnum;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberEntity;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@NoArgsConstructor
@Builder
class AvailabilityCrudFinder {

    private AvailabilityCrudSaver availabilityCrudSaver;
    private AvailabilityCrudRepository availabilityRepository;
    private AvailabilityMemberFinder memberFinder;

    public AvailabilityEntity findEntityByIdOrByMember(AvailabilityDto dto) throws AvailabilityNotFoundException, MemberNotFoundException, UserNotFoundException {
        if (dto.getId() != null)
            return findById(dto.getId())
                    .orElseThrow(() -> new AvailabilityNotFoundException(AvailabilityCrudInfoStatusEnum.AVAILABILITY_NOT_FOUND.toString()));
        else
            return findAvailabilityByMember(dto)
                    .orElseThrow(() -> new AvailabilityNotFoundException(AvailabilityCrudInfoStatusEnum.AVAILABILITY_NOT_FOUND.toString()));
    }

    public Optional<AvailabilityEntity> findById(Long id) {
        return availabilityRepository.findById(id);
    }

    private MemberEntity findMemberEntity(MemberDto member) throws MemberNotFoundException, UserNotFoundException {
        return memberFinder.findMember(member);
    }

    private Optional<AvailabilityEntity> findAvailabilityByMember(AvailabilityDto dto) throws MemberNotFoundException, UserNotFoundException {
        MemberEntity member = findMemberEntity(dto.getMember());

        return availabilityRepository.findByMember(member);
    }

    public List<AvailabilityEntity> findEntitiesByMember(AvailabilityDto dto) throws AvailabilityNotFoundException, MemberNotFoundException, UserNotFoundException {
            return findAvailabilitiesByMember(dto)
                    .orElseThrow(() -> new AvailabilityNotFoundException(AvailabilityCrudInfoStatusEnum.AVAILABILITY_NOT_FOUND.toString()));
    }

    private Optional<List<AvailabilityEntity>> findAvailabilitiesByMember(AvailabilityDto dto) throws MemberNotFoundException, UserNotFoundException {
        MemberEntity member = findMemberEntity(dto.getMember());

        return availabilityRepository.findAllByMember(member);
    }

    public boolean isExist(AvailabilityDto dto) {
        try {
            return findAvailabilityByMember(dto).isPresent();
        } catch (MemberNotFoundException | UserNotFoundException e) {
            return false;
        }
    }
}
