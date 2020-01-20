package pl.com.devmeet.devmeetcore.member_associated.place;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceCrudFacade;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceDtoApi;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceMapperApi;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.status_and_exceptions.PlaceNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
class PlaceApi {

    private PlaceCrudFacade place;
    private PlaceMapperApi modelMapper;
    private List<PlaceDtoApi> placeDtoApiList;

    @Autowired
    PlaceApi(PlaceCrudFacade place, PlaceMapperApi modelMapper) {
        this.place = place;
        this.modelMapper = modelMapper;
        placeDtoApiList = new ArrayList<>();
    }

    @GetMapping
    public ResponseEntity<List<PlaceDtoApi>> getPlaces() throws MemberNotFoundException, PlaceNotFoundException, UserNotFoundException {

        placeDtoApiList.clear();
        placeDtoApiList.add(modelMapper.getModelMapper().map(place.findAll().stream().findAny().get(), PlaceDtoApi.class));
        return new ResponseEntity<>(placeDtoApiList, HttpStatus.OK);
    }
}
