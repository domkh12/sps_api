package edu.npic.sps.mapper;

import edu.npic.sps.domain.Vehicle;
import edu.npic.sps.domain.VehicleType;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import edu.npic.sps.features.vehicle.dto.VehicleTypeResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleResponse toVehicleResponse(Vehicle vehicle);

    List<VehicleTypeResponse> toVehicleTypeResponse(List<VehicleType> vehicleTypes);

    VehicleTypeResponse toVehicleTypeResponse(VehicleType vehicleType);

}
