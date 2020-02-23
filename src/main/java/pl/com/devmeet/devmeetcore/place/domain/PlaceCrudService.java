package pl.com.devmeet.devmeetcore.place.domain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudRepository;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberRepository;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceAlreadyExistsException;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceFoundButNotActiveException;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceNotFoundException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain.MessengerRepository;
import pl.com.devmeet.devmeetcore.user.domain.UserRepository;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import java.util.List;

import static pl.com.devmeet.devmeetcore.place.domain.PlaceCrudMapper.mapDtoList;

@Service
public class PlaceCrudService {

    private PlaceCrudRepository placeRepository;
    private MemberRepository memberRepository;
    private UserRepository userRepository;
    private MessengerRepository messengerRepository;
    private GroupCrudRepository groupCrudRepository;

    @Autowired
    public PlaceCrudService(PlaceCrudRepository placeRepository, MemberRepository memberRepository, UserRepository userRepository, MessengerRepository messengerRepository, GroupCrudRepository groupCrudRepository) {
        this.placeRepository = placeRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.messengerRepository = messengerRepository;
        this.groupCrudRepository = groupCrudRepository;
    }


    private PlaceMemberFinder initMemberFinder() {
        return PlaceMemberFinder.builder()
                .memberCrudService(new MemberCrudService(memberRepository, userRepository, messengerRepository, groupCrudRepository))
                .build();
    }


    private PlaceCrudCreator initCreator() {
        return PlaceCrudCreator.builder()
                .placeCrudFinder(initFinder())
                .placeCrudSaver(initSaver())
                .placeMemberFinder(initMemberFinder())
                .build();
    }

    private PlaceCrudSaver initSaver() {
        return PlaceCrudSaver.builder()
                .placeCrudRepository(placeRepository)
                .memberFinder(initMemberFinder())
                .build();
    }

    private PlaceCrudFinder initFinder() {
        return PlaceCrudFinder.builder()
                .placeRepository(placeRepository)
                .memberFinder(initMemberFinder())
                .build();
    }

    private PlaceCrudUpdater initUpdater() {
        return PlaceCrudUpdater.builder()
                .placeCrudFinder(initFinder())
                .placeCrudSaver(initSaver())
                .build();
    }

    private PlaceCrudDeleter initDeleter() {

        return PlaceCrudDeleter.builder()
                .placeCrudFinder(initFinder())
                .placeCrudSaver(initSaver())
                .build();
    }

    public PlaceCrudFinder getEntityFinder() {
        return initFinder();
    }

    public PlaceDto findPlaceById(Long id) throws PlaceNotFoundException {
        return map(initFinder().findPlaceById(id));
    }

    public PlaceDto findPlaceByIdOrFeatures(PlaceDto dto) throws PlaceNotFoundException {
        return map(initFinder().findPlaceFeatures(dto));
    }

    public List<PlaceDto> findAllBySearchingText(String text) {
        return mapDtoList(initFinder().findAllBySearchingText(text));
    }

    public List<PlaceDto> findAll() {
        return mapDtoList(initFinder().findAllEntities());
    }

    public boolean isExist(PlaceDto dto){
        return initFinder().isExist(dto);
    }

    public PlaceDto add(PlaceDto dto) throws MemberNotFoundException, UserNotFoundException, PlaceAlreadyExistsException {
        return map(initCreator().createEntity(dto));
    }

    public PlaceDto update(PlaceDto toUpdate) throws MemberNotFoundException, PlaceNotFoundException, UserNotFoundException {
        return map(initUpdater().updateEntity(toUpdate));
    }

    public PlaceDto delete(PlaceDto dto) throws UserNotFoundException, MemberNotFoundException, PlaceNotFoundException, PlaceFoundButNotActiveException {
        return map(initDeleter().deleteEntity(dto));
    }

    public static PlaceDto map(PlaceEntity entity) {
        return PlaceCrudMapper.map(entity);
    }

    public static PlaceEntity map(PlaceDto dto) {
        return PlaceCrudMapper.map(dto);

    }
}
