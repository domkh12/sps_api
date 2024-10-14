package edu.npic.sps.features.user;

import edu.npic.sps.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.isDeleted = false")
    List<User> findAllNotDeleted();

    Optional<User> findByUuid(String uuid);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Boolean existsByUuid(String uuid);

    Boolean existsByPhoneNumber(String phoneNumber);
}
