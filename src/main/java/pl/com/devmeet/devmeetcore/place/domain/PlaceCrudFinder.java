package pl.com.devmeet.devmeetcore.place.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberEntity;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceCrudStatusEnum;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
class PlaceCrudFinder {

    private PlaceCrudSaver placeCrudSaver;
    private PlaceCrudRepository placeRepository;
    private PlaceMemberFinder memberFinder;

    private MemberEntity findMemberEntity(MemberDto member) throws MemberNotFoundException, UserNotFoundException {
        return memberFinder.findMember(member);
    }

    public List<PlaceEntity> findPlaceByIdOrFeatures(PlaceDto placeDto) throws PlaceNotFoundException {
        List<PlaceEntity> resultList = new ArrayList<>();
        if (placeDto.getId() != null)
            resultList.add(findPlaceById(placeDto.getId()));
        else {
            resultList.addAll(findAllBySearchingText(placeDto.getPlaceName()));
            resultList.addAll(findAllBySearchingText(placeDto.getDescription()));
            resultList.addAll(findAllBySearchingText(placeDto.getWebsite()));
            resultList.addAll(findAllBySearchingText(placeDto.getLocation()));
        }
        return resultList;
    }

    public List<PlaceDto> findPlaceByIdOrFeaturesAndMapToDto(PlaceDto placeDto) throws PlaceNotFoundException {
        return mapToDtoList(findPlaceByIdOrFeatures(placeDto));
    }

    public PlaceEntity findPlaceById(Long id) throws PlaceNotFoundException {
        if (id != null)
            return placeRepository.findById(id).get();
        else
            throw new PlaceNotFoundException(PlaceCrudStatusEnum.PLACE_NOT_FOUND.toString());
    }

    public PlaceDto findPlaceByIdAndMapToDto(Long id) throws PlaceNotFoundException {
        return mapToDto(findPlaceById(id));
    }

//    public List<PlaceEntity> findAllPlacesByMember(MemberDto memberDto) throws MemberNotFoundException, UserNotFoundException, PlaceNotFoundException {
//        MemberEntity foundMember = findMemberEntity(memberDto);
//        return placeRepository.findAllByMember(foundMember)
//                .orElseThrow(() -> new PlaceNotFoundException(PlaceCrudStatusEnum.PLACES_NOT_FOUND_BY_MEMBER.toString()));
//    }
//
//    public List<PlaceDto> findAllPlacesByMemberAndMapToDto(MemberDto memberDto) throws MemberNotFoundException, PlaceNotFoundException, UserNotFoundException {
//        return mapToDtoList(findAllPlacesByMember(memberDto));
//    }

    public List<PlaceEntity> findAllBySearchingText(String text) {
        return placeRepository.findAllBySearchText(text);
    }

    public List<PlaceDto> findAllBySearchingTextAndMapToDto(String text) {
        return mapToDtoList(findAllBySearchingText(text));
    }

    public List<PlaceEntity> findAllEntities() {
        return placeRepository.findAll();
    }

    public List<PlaceDto> findAllEntitiesAndMapToDto() {
        return mapToDtoList(findAllEntities());
    }

    public boolean isExist(PlaceDto dto) {
        try {
            return findPlaceById(dto.getId()) != null;
        } catch (PlaceNotFoundException e) {
            return false;
        }
    }

    public PlaceDto mapToDto(PlaceEntity entity) {
        return PlaceCrudMapper.map(entity);
    }

    private List<PlaceDto> mapToDtoList(List<PlaceEntity> entities) {
        return PlaceCrudMapper.mapDtoList(entities);
    }
}