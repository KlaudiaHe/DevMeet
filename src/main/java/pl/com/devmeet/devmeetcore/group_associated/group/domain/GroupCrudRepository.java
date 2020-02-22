package pl.com.devmeet.devmeetcore.group_associated.group.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupCrudRepository extends JpaRepository<GroupEntity, Long> {

    Optional<GroupEntity> findByGroupName(String groupName);

    @Query("select g from GroupEntity g where lower(g.groupName) like lower(concat('%',:search,'%') ) " +
            "or g.website like concat('%',:search,'%') " +
            "or lower(g.description) like lower(concat('%',:search,'%'))")
    List<GroupEntity> findAllBySearchText(String search);

    @Query("select g from GroupEntity g where g.isActive=:isActive")
    List<GroupEntity> findAllByActive(Boolean isActive);

}
