package pl.com.devmeet.devmeetcore.api;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceDto;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceDtoApi;

@Component
class PlaceMapperApi {

    ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<PlaceDto, PlaceDtoApi>() {
            @Override
            protected void configure() {
                map().setUserEmail(source.getMember().getUser().getEmail());
                map().setPlaceName(source.getPlaceName());
            }
        });
        return modelMapper;
    }
}
