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
                map().setId(source.getId());
                map().setUserEmail(source.getMember().getUser().getEmail());
                map().setPlaceName(source.getPlaceName());
                map().setDescription(source.getDescription());
                map().setWebsite(source.getWebsite());
                map().setLocation(source.getLocation());
                map().setCreationTime(source.getCreationTime());
                map().setModificationTime(source.getModificationTime());
                map().setActive(source.isActive());
            }
        });
        return modelMapper;
    }
}
