package pl.com.devmeet.devmeetcore.place.domain;

import java.util.List;
import java.util.stream.Collectors;

class PlaceCrudMapper {

    public static PlaceDto map(PlaceEntity entity) {
        return entity != null ? new PlaceDto().builder()
                .id(entity.getId())
                .placeName(entity.getPlaceName())
                .description(entity.getDescription())
                .website(entity.getWebsite())
                .location(entity.getLocation())
//                .placeVotes(entity.getPlaceVotes())
                .creationTime(entity.getCreationTime())
                .modificationTime(entity.getModificationTime())
                .isActive(entity.isActive())
                .build() : null;
    }

    public static PlaceEntity map(PlaceDto dto) {
        return dto != null ? new PlaceEntity().builder()
                .id(dto.getId())
                .placeName(dto.getPlaceName())
                .description(dto.getDescription())
                .website(dto.getWebsite())
                .location(dto.getLocation())
//                .placeVotes(dto.getPlaceVotes())
                .creationTime(dto.getCreationTime())
                .modificationTime(dto.getModificationTime())
                .isActive(dto.isActive())
                .build() : null;
    }

    public static List<PlaceDto> mapDtoList(List<PlaceEntity> entities) {
        return entities.parallelStream()
                .map(PlaceCrudMapper::map)
                .collect(Collectors.toList());
    }

    public static List<PlaceEntity> mapEntityList(List<PlaceDto> dtos) {
        return dtos.parallelStream()
                .map(PlaceCrudMapper::map)
                .collect(Collectors.toList());
    }
}

