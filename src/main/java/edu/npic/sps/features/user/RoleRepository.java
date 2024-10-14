package edu.npic.sps.features.user;

import edu.npic.sps.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
//    List<Role> findByRoleName(String roleName);
    Optional<Role> findByName(String name);
}
