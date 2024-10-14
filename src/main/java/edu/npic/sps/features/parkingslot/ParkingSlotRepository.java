package edu.npic.sps.features.parkingslot;

import edu.npic.sps.domain.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Integer> {
    Optional<ParkingSlot> findByUuid(String uuid);
    Optional<ParkingSlot> findByAlias(String alias);
}
