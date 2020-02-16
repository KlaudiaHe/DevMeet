package pl.com.devmeet.devmeetcore.member_associated.availability;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.AvailabilityCrudService;

@Component
public class AvailabilityFacade {

    @Getter
    private AvailabilityCrudService availabilityCrudService;

    @Autowired
    private AvailabilityFacade(AvailabilityCrudService availabilityCrudService) {
        this.availabilityCrudService = availabilityCrudService;
    }

}
