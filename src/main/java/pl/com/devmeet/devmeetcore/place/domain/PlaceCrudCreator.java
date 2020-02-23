package pl.com.devmeet.devmeetcore.place.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import pl.com.devmeet.devmeetcore.domain_utils.CrudEntityCreator;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceAlreadyExistsException;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceCrudStatusEnum;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
class PlaceCrudCreator implements CrudEntityCreator<PlaceDto, PlaceEntity> {

    private PlaceCrudSaver placeCrudSaver;
    private PlaceCrudFinder placeCrudFinder;
    private PlaceMemberFinder placeMemberFinder;

    @Override
    public PlaceEntity createEntity(PlaceDto dto) throws PlaceAlreadyExistsException {
        PlaceEntity place;
        try {
            place = placeCrudFinder.findPlaceFeatures(dto);

            if (!place.isActive() && place.getModificationTime() != null)
                return placeCrudSaver.saveEntity(setDefaultValuesWhenPlaceExists(place));

        } catch (PlaceNotFoundException e) {
            place = setDefaultValuesWhenPlaceNotExists(PlaceCrudService.map(dto));
            return placeCrudSaver.saveEntity(place);
        }

        throw new PlaceAlreadyExistsException(PlaceCrudStatusEnum.PLACE_ALREADY_EXISTS.toString());
    }

    private PlaceEntity setDefaultValuesWhenPlaceNotExists(PlaceEntity place) {
        place.setCreationTime(DateTime.now());
        place.setActive(true);
        return place;
    }

    private PlaceEntity setDefaultValuesWhenPlaceExists(PlaceEntity place) {
        place.setModificationTime(DateTime.now());
        place.setActive(true);
        return place;

    }
}
