package edu.npic.sps.features.vehicle;

import edu.npic.sps.features.vehicle.dto.CreateVehicleType;
import edu.npic.sps.features.vehicle.dto.VehicleTypeRequest;
import edu.npic.sps.features.vehicle.dto.VehicleTypeResponse;

import java.util.List;

public interface VehicleTypeService {

    void deleteByAlias(String alias);

    VehicleTypeResponse updateByAlias(String alias, VehicleTypeRequest vehicleTypeRequest);

    void createNew(CreateVehicleType createVehicleType);

    List<VehicleTypeResponse> findAll();
}
