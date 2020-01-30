package pl.com.devmeet.devmeetcore.api.member_associated.availability;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.AvailabilityDto;

@Component
class AvailabilityMapperApi {

    public ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(new PropertyMap<AvailabilityDto, AvailabilityDtoApi>() {
            @Override
            protected void configure() {


            }
        });
        return mapper;
    }
}
