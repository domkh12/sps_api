package edu.npic.sps.features.parkingslot;

import edu.npic.sps.domain.ParkingSlotDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSlotDetailRepository extends JpaRepository<ParkingSlotDetail, Integer> {
}
