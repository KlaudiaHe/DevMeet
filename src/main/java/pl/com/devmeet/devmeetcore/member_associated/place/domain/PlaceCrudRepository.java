package pl.com.devmeet.devmeetcore.member_associated.place.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceCrudRepository extends PagingAndSortingRepository<PlaceEntity, Long> {

    Optional<PlaceEntity> findByMemberAndPlaceName(MemberEntity member, String placeName);

    Optional<PlaceEntity> findByMemberAndDescription(MemberEntity member, String description);

    Optional<PlaceEntity> findByMemberAndWebsite(MemberEntity member, String website);

    Optional<PlaceEntity> findByMemberAndLocation(MemberEntity member, String location);

    Optional<PlaceEntity> findByMemberAndPlaceNameAndDescriptionAndWebsiteAndLocation(MemberEntity member,
                                                                                      String placeName,
                                                                                      String description,
                                                                                      String website,
                                                                                      String location);

    Optional<List<PlaceEntity>> findAllByMember(MemberEntity member);
}
