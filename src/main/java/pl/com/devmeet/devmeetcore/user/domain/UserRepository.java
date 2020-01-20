package pl.com.devmeet.devmeetcore.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailAndPassword(String email, String password);

    Optional<UserEntity> findByEmail(String email);

    @Query("select u from UserEntity u where lower(u.email) like lower(concat('%', :search, '%') )")
    List<UserEntity> findEmailLike(String search);


    @Query("select u from UserEntity u where u.isActive = :isActive")
    List<UserEntity> findAllByIsActive(@Param("isActive") Boolean isActive);

}
