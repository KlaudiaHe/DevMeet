package pl.com.devmeet.devmeetcore.place.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceCrudRepository extends JpaRepository<PlaceEntity, Long> {

    @Query("select p from PlaceEntity p where "+
            "lower(p.placeName) like lower(concat('%',:search,'%') ) " +
            "or lower(p.description) like lower(concat('%',:search,'%')) " +
            "or p.website like concat('%',:search,'%') " +
            "or lower(p.location) like lower(concat('%',:search,'%'))")
    List<PlaceEntity> findAllBySearchText(String search);

    Optional<PlaceEntity> findByPlaceNameAndDescriptionAndWebsiteAndLocation(String placeName, String description, String website, String location);
}
