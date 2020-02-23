package pl.com.devmeet.devmeetcore.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.com.devmeet.devmeetcore.place.domain.PlaceCrudService;
import pl.com.devmeet.devmeetcore.place.domain.PlaceDto;
import pl.com.devmeet.devmeetcore.place.domain.PlaceDtoApi;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceNotFoundException;

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
    public ResponseEntity<List<PlaceDtoApi>> getPlaces() {
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
        try {
            PlaceDto foundPlace = place.findPlaceById(id);
            PlaceDtoApi placeDtoApi = modelMapper.getModelMapper()
                    .map(foundPlace, PlaceDtoApi.class);
            return new ResponseEntity<>(placeDtoApi, HttpStatus.OK);

        } catch (PlaceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }
    }

}
