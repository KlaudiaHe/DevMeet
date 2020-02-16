package pl.com.devmeet.devmeetcore.member_associated.availability.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.devmeet.devmeetcore.domain_utils.CrudFacadeInterface;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudRepository;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityException;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberRepository;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain.MessengerRepository;
import pl.com.devmeet.devmeetcore.user.domain.UserRepository;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

import static pl.com.devmeet.devmeetcore.member_associated.availability.domain.AvailabilityCrudMapper.mapDtoList;

@Service
public class AvailabilityCrudService implements CrudFacadeInterface<AvailabilityDto, AvailabilityEntity> {

    private AvailabilityCrudRepository availabilityRepository;
    private MemberRepository memberRepository;
    private UserRepository userRepository;
    private MessengerRepository messengerRepository;
    private GroupCrudRepository groupCrudRepository;

    @Autowired
    public AvailabilityCrudService(AvailabilityCrudRepository availabilityRepository, MemberRepository memberRepository, UserRepository userRepository, MessengerRepository messengerRepository, GroupCrudRepository groupCrudRepository) {
        this.availabilityRepository = availabilityRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.messengerRepository = messengerRepository;
        this.groupCrudRepository = groupCrudRepository;
    }

    private AvailabilityMemberFinder initMemberFinder() {
        return AvailabilityMemberFinder.builder()
                .memberCrudService(new MemberCrudService(memberRepository, userRepository, messengerRepository, groupCrudRepository))
                .build();
    }

    private AvailabilityCrudCreator initCreator() {
        return AvailabilityCrudCreator.builder()
                .availabilityCrudFinder(initFinder())
                .availabilityCrudSaver(initSaver())
                .build();
    }

    private AvailabilityCrudSaver initSaver() {
        return AvailabilityCrudSaver.builder()
                .availabilityCrudRepository(availabilityRepository)
                .memberFinder(initMemberFinder())
                .build();
    }

    private AvailabilityCrudFinder initFinder() {
        return AvailabilityCrudFinder.builder()
                .availabilityRepository(availabilityRepository)
                .memberFinder(initMemberFinder())
                .build();
    }

    private AvailabilityCrudUpdater initUpdater() {
        return AvailabilityCrudUpdater.builder()
                .availabilityCrudFinder(initFinder())
                .availabilityCrudSaver(initSaver())
                .build();
    }

    private AvailabilityCrudDeleter initDeleter() {

        return AvailabilityCrudDeleter.builder()
                .availabilityCrudFinder(initFinder())
                .availabilityCrudSaver(initSaver())
                .build();
    }


    @Override
    public AvailabilityDto add(AvailabilityDto dto) throws MemberNotFoundException, AvailabilityAlreadyExistsException, UserNotFoundException {
        return map(initCreator().createEntity(dto));
    }

    public AvailabilityDto find(AvailabilityDto dto) throws MemberNotFoundException, AvailabilityNotFoundException, UserNotFoundException {
        return map(initFinder().findEntityByIdOrByMember(dto));
    }

    public Optional<AvailabilityDto> findById (Long id){
        return initFinder().findById(id)
                .map(AvailabilityCrudService::map);
    }

    public List<AvailabilityDto> findAll(AvailabilityDto dto) throws MemberNotFoundException, AvailabilityNotFoundException, UserNotFoundException {
        return mapDtoList(initFinder().findEntitiesByMember(dto));
    }

    public List<AvailabilityDto> findAllByMemberId(Long memberId) throws MemberNotFoundException, AvailabilityNotFoundException, UserNotFoundException {
        AvailabilityDto availabilityDto = AvailabilityDto.builder()
                .id(memberId)
                .build();
        return findAll(availabilityDto);
    }

    @Override
    public AvailabilityDto update(AvailabilityDto oldDto, AvailabilityDto newDto) throws UserNotFoundException, AvailabilityNotFoundException, AvailabilityException, MemberNotFoundException {
        return map(initUpdater().updateEntity(oldDto, newDto));
    }

    public AvailabilityDto updateByAvailabilityId(AvailabilityDto newDto) throws UserNotFoundException, MemberNotFoundException, AvailabilityException, AvailabilityNotFoundException {
        return map(initUpdater().updateEntityById(newDto));
    }

    @Override
    public AvailabilityDto delete(AvailabilityDto dto) throws UserNotFoundException, AvailabilityNotFoundException, MemberNotFoundException, AvailabilityAlreadyExistsException {
        return map(initDeleter().deleteEntity(dto));
    }

    public AvailabilityEntity findEntity(AvailabilityDto dto) throws MemberNotFoundException, AvailabilityNotFoundException, UserNotFoundException {
        return initFinder().findEntityByIdOrByMember(dto);
    }

    public List<AvailabilityEntity> findEntities(AvailabilityDto dto) throws MemberNotFoundException, AvailabilityNotFoundException, UserNotFoundException {
        return initFinder().findEntitiesByMember(dto);
    }

    public static AvailabilityDto map(AvailabilityEntity entity) {
        return AvailabilityCrudMapper.map(entity);
    }


    public static AvailabilityEntity map(AvailabilityDto dto) {
        return AvailabilityCrudMapper.map(dto);

    }
}
