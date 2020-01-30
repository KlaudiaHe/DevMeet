package pl.com.devmeet.devmeetcore.api.member_associated.availability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.devmeet.devmeetcore.member_associated.availability.AvailabilityFacade;

@RestController
@RequestMapping("/api/v1/availabilities")
class AvailabilityApi {

    private AvailabilityFacade availabilityFacade;

    @Autowired
    private AvailabilityApi(AvailabilityFacade availabilityFacade) {
        this.availabilityFacade = availabilityFacade;
    }

}
