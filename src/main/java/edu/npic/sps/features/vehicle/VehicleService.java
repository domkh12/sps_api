package edu.npic.sps.features.vehicle;

import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import org.springframework.data.domain.Page;

public interface VehicleService {

    VehicleResponse findByNumPlate(String numberPlate);

    Page<VehicleResponse> findAll(int pageNo, int pageSize);
}
