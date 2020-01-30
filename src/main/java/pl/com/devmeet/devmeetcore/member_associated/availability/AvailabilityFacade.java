package pl.com.devmeet.devmeetcore.member_associated.availability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.AvailabilityCrudService;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.AvailabilityDto;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

@Component
public class AvailabilityFacade {

    private AvailabilityCrudService availabilityCrudService;

    @Autowired
    private AvailabilityFacade(AvailabilityCrudService availabilityCrudService) {
        this.availabilityCrudService = availabilityCrudService;
    }

    public AvailabilityDto addNewAvailability(AvailabilityDto availabilityDto) throws MemberNotFoundException, AvailabilityAlreadyExistsException, UserNotFoundException {
        return availabilityCrudService.add(availabilityDto);
    }
}
