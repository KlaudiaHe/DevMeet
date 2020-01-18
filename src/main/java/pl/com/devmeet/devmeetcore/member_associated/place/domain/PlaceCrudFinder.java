package pl.com.devmeet.devmeetcore.member_associated.place.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.com.devmeet.devmeetcore.domain_utils.CrudEntityFinder;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberEntity;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.status_and_exceptions.PlaceCrudStatusEnum;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.status_and_exceptions.PlaceNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Builder
class PlaceCrudFinder implements CrudEntityFinder<PlaceDto, PlaceEntity> {

    private PlaceCrudSaver placeCrudSaver;
    private PlaceCrudRepository placeRepository;
    private PlaceMemberFinder memberFinder;

    @Override
    public PlaceEntity findEntity(PlaceDto dto) throws PlaceNotFoundException, MemberNotFoundException, UserNotFoundException {
        return findPlace(dto);
    }

    private MemberEntity findMemberEntity(MemberDto member) throws MemberNotFoundException, UserNotFoundException {
        return memberFinder.findMember(member);
    }

    private MemberDto getMemberFromDto(PlaceDto dto) {
        try {
            return dto.getMember();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public List<PlaceEntity> findAllEntitiesByMember(MemberDto memberDto) throws MemberNotFoundException, UserNotFoundException, PlaceNotFoundException {
        Optional<List<PlaceEntity>> foundPlaces = findPlaces(memberDto);

        if (foundPlaces.isPresent())
            return foundPlaces.get();
        else
            throw new PlaceNotFoundException(PlaceCrudStatusEnum.PLACES_NOT_FOUND.toString());
    }

    private PlaceEntity findPlace(PlaceDto dto) throws MemberNotFoundException, UserNotFoundException, PlaceNotFoundException {
        MemberEntity member = findMemberEntity(getMemberFromDto(dto));
        Optional<PlaceEntity> foundPlace = placeRepository.findByMemberAndPlaceNameAndDescriptionAndWebsiteAndLocation(member, dto.getPlaceName(), dto.getDescription(), dto.getLocation(), dto.getWebsite());

        if (foundPlace.isPresent())
            return foundPlace.get();
        else
            throw new PlaceNotFoundException(PlaceCrudStatusEnum.PLACE_NOT_FOUND.toString());
    }

    private Optional<List<PlaceEntity>> findPlaces(MemberDto memberDto) throws MemberNotFoundException, UserNotFoundException {
        MemberEntity member = findMemberEntity(memberDto);

        return placeRepository.findAllByMember(member);
    }

    @Deprecated
    @Override
    public List<PlaceEntity> findEntities(PlaceDto dto) throws PlaceNotFoundException, MemberNotFoundException, UserNotFoundException {
        return findAllEntitiesByMember(getMemberFromDto(dto));
    }

    public List<PlaceEntity> findAllEntities() {
        List<PlaceEntity> entities = new ArrayList<>();
        placeRepository.findAll().forEach(entities::add);
        return entities;
    }

    @Override
    public boolean isExist(PlaceDto dto) {
        try {
            return findEntity(dto) != null;
        } catch (PlaceNotFoundException | MemberNotFoundException | UserNotFoundException e) {
            return false;
        }
    }

}