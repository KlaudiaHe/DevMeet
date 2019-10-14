package pl.com.devmeet.devmeet.member_associated.availability.domain;

import pl.com.devmeet.devmeet.domain_utils.CrudEntityCreator;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import pl.com.devmeet.devmeet.member_associated.availability.domain.status.AvailabilityCrudInfoStatusEnum;


@AllArgsConstructor
class AvailabilityCrudCreator implements CrudEntityCreator<AvailabilityDto, AvailabilityEntity> {

    private AvailabilityCrudSaver saver;
    private AvailabilityCrudFinder finder;

    public AvailabilityCrudCreator(AvailabilityCrudRepository repository) {
        this.saver = new AvailabilityCrudSaver(repository);
        this.finder = new AvailabilityCrudFinder(repository);

    }

    @Override
    public AvailabilityEntity createEntity(AvailabilityDto dto) {
        AvailabilityEntity availability;
        boolean availabilityActivity;

        try {
            availability = finder.findEntity(dto);
            availabilityActivity = availability.isActive();

            if (!availabilityActivity && availability.getModificationTime() != null)
                //    return setDefaultValuesWhenAvailabilityExists(saver.saveEntity(availability));
                return saver.saveEntity(setDefaultValuesWhenAvailabilityExists(availability));

        } catch (IllegalArgumentException e) {
            //  return setDefaultValuesWhenAvailabilityNotExists(availability);
            return saver.saveEntity(setDefaultValuesWhenAvailabilityNotExists(AvailabilityCrudFacade.map(dto)));
        }

        throw new IllegalArgumentException(AvailabilityCrudInfoStatusEnum.AVAILABILITY_ALREADY_EXISTS.toString());
    }

    private AvailabilityEntity setDefaultValuesWhenAvailabilityNotExists(AvailabilityEntity availability) {
        availability.setCreationTime(DateTime.now());
        availability.setActive(true);
        return availability;
    }

    private AvailabilityEntity setDefaultValuesWhenAvailabilityExists(AvailabilityEntity entity) {
        entity.setModificationTime(DateTime.now());
        entity.setActive(true);
        return entity;
    }
}

