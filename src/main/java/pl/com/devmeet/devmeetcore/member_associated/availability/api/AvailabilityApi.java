package pl.com.devmeet.devmeetcore.member_associated.availability.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.AvailabilityCrudService;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.AvailabilityDto;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityException;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/availabilities")
public class AvailabilityApi {

    private AvailabilityCrudService availabilityService;
    private AvailabilityApiMapper mapperApi = new AvailabilityApiMapper();

    @Autowired
    public AvailabilityApi(AvailabilityCrudService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    public ResponseEntity<AvailabilityApiDto> addNew(@RequestBody AvailabilityApiDto newAvailability) {
        try {
            AvailabilityDto added = availabilityService.add(mapperApi.mapToBackend(newAvailability));

            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/id")
                    .buildAndExpand(added.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(mapperApi.mapToFrontend(added));

        } catch (MemberNotFoundException | AvailabilityAlreadyExistsException | UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Can't add this availability. Error:  " + e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<List<AvailabilityApiDto>> getMemberAvailabilities(@PathVariable Long memberId) {
        try {
            return new ResponseEntity<>(mapperApi.mapListToFrontend(
                    availabilityService.findAllByMemberId(memberId)),
                    HttpStatus.FOUND);

        } catch (MemberNotFoundException | AvailabilityNotFoundException | UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    ("Resource with id(" + memberId + ") not found becouse: " + e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<AvailabilityApiDto> update(@RequestBody AvailabilityApiDto apiDto) {
        try {
            return availabilityService
                    .update(mapperApi.mapToBackend(apiDto)) != null ?
                    new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.FORBIDDEN);

        } catch (UserNotFoundException | MemberNotFoundException | AvailabilityException | AvailabilityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    ("Can't update this availability. Error: " + e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<AvailabilityApiDto> delete(@RequestBody AvailabilityApiDto apiDto) {
        try {
            return availabilityService
                    .delete(mapperApi.mapToBackend(apiDto)) != null ?
                    new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.FORBIDDEN);

        } catch (UserNotFoundException | AvailabilityNotFoundException | MemberNotFoundException | AvailabilityAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    ("Can't delete this availability. Error: " + e.getMessage()));
        }
    }
}
