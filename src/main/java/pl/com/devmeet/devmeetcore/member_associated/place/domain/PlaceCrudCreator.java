package pl.com.devmeet.devmeetcore.member_associated.place.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import pl.com.devmeet.devmeetcore.domain_utils.CrudEntityCreator;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudFacade;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberEntity;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.status_and_exceptions.PlaceAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.status_and_exceptions.PlaceCrudStatusEnum;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.status_and_exceptions.PlaceNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

@AllArgsConstructor
@NoArgsConstructor
@Builder
class PlaceCrudCreator implements CrudEntityCreator<PlaceDto, PlaceEntity> {

    private PlaceCrudSaver placeCrudSaver;
    private PlaceCrudFinder placeCrudFinder;
    private PlaceMemberFinder placeMemberFinder;

    @Override
    public PlaceEntity createEntity(PlaceDto dto) throws MemberNotFoundException, UserNotFoundException, PlaceAlreadyExistsException {
        PlaceEntity place;
        MemberEntity foundMember = placeMemberFinder.findMember(getSafeMemberDto(dto));
        try {
            place = placeCrudFinder.findEntity(dto);

            if (!place.isActive() && place.getModificationTime() != null)
                return placeCrudSaver.saveEntity(setDefaultValuesWhenPlaceExists(place));

        } catch (PlaceNotFoundException e) {
            place = setDefaultValuesWhenPlaceNotExists(PlaceCrudFacade.map(dto));
            return placeCrudSaver.saveEntity(
                    connectPlaceWithMember(place, foundMember)
            );
        }

        throw new PlaceAlreadyExistsException(PlaceCrudStatusEnum.PLACE_ALREADY_EXISTS.toString());
    }

    private MemberDto getSafeMemberDto(PlaceDto placeDto) {
        try {
            return placeDto.getMember();
        } catch (NullPointerException e) {
            return null;
        }
    }

    private PlaceEntity setDefaultValuesWhenPlaceNotExists(PlaceEntity place) {
        place.setCreationTime(DateTime.now());
        place.setActive(true);
        return place;
    }

    private PlaceEntity connectPlaceWithMember(PlaceEntity placeEntity, MemberEntity memberEntity) {
        return new PlaceMemberConnector()
                .connect(placeEntity, memberEntity);
    }

    private PlaceEntity setDefaultValuesWhenPlaceExists(PlaceEntity place) {
        place.setModificationTime(DateTime.now());
        place.setActive(true);
        return place;

    }
}
