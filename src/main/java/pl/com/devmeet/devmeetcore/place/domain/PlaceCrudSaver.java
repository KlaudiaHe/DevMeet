package pl.com.devmeet.devmeetcore.place.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.com.devmeet.devmeetcore.domain_utils.CrudEntitySaver;

@Builder
@AllArgsConstructor
@NoArgsConstructor
class PlaceCrudSaver implements CrudEntitySaver<PlaceEntity, PlaceEntity> {

    private PlaceCrudRepository placeCrudRepository;
    private PlaceMemberFinder memberFinder;

    @Override
    public PlaceEntity saveEntity(PlaceEntity entity) {
        return placeCrudRepository
                .save(entity);
    }

//    private PlaceEntity connectPlaceWithMember(PlaceEntity placeEntity) throws MemberNotFoundException, UserNotFoundException {
//        MemberEntity memberEntity = placeEntity.getMember();
//        if (memberEntity.getId() == null)
//            memberEntity= memberFinder.findMember(MemberCrudService.map(placeEntity.getMember()));
//        placeEntity.setMember(memberEntity);
//        return placeEntity;
//    }

}