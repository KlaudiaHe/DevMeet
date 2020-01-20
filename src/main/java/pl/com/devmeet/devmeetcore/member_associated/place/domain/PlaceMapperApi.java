package pl.com.devmeet.devmeetcore.member_associated.place.domain;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class PlaceMapperApi {

    public ModelMapper getModelMapper() {
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
