package pl.com.devmeet.devmeetcore.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceCrudService;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceDto;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceDtoApi;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.status_and_exceptions.PlaceNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/places")
class PlaceApi {

    private PlaceCrudService place;
    private PlaceMapperApi modelMapper;
    private List<PlaceDtoApi> placeDtoApiList;

    @Autowired
    PlaceApi(PlaceCrudService place, PlaceMapperApi modelMapper) {
        this.place = place;
        this.modelMapper = modelMapper;
        placeDtoApiList = new ArrayList<>();
    }

    @GetMapping
    public ResponseEntity<List<PlaceDtoApi>> getPlaces() throws MemberNotFoundException, PlaceNotFoundException, UserNotFoundException {
        placeDtoApiList.clear();
        for (PlaceDto placeDto : place.findAll()) {
            placeDtoApiList.add(modelMapper
                    .getModelMapper()
                    .map(placeDto, PlaceDtoApi.class));
        }
        return new ResponseEntity<>(placeDtoApiList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<PlaceDtoApi> getById(@PathVariable Long id) {
        if (place.findById(id).isPresent()) {
            PlaceDtoApi placeDtoApi = new PlaceDtoApi();
            placeDtoApi = modelMapper.getModelMapper()
                    .map(place.findById(id).get(), PlaceDtoApi.class);
            return new ResponseEntity<>(placeDtoApi, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);


    }

}
