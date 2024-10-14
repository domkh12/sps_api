package edu.npic.sps.features.vehicle;

import edu.npic.sps.domain.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Integer> {
    Boolean existsByAlias(String alias);
    Optional<VehicleType> findByAlias(String alias);
}
