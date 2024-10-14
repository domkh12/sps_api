package edu.npic.sps.features.parkingslot;

import edu.npic.sps.features.parkingslot.dto.CreateCarParking;
import edu.npic.sps.features.parkingslot.dto.ParkingSlotDetailResponse;
import org.springframework.data.domain.Page;

public interface ParkingSlotService {

    Page<ParkingSlotDetailResponse> findAll(int pageNo, int pageSize);

    void createCarParking(CreateCarParking createCarParking);

    ParkingSlotDetailResponse findByUuid(String uuid);
}
