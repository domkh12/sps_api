package edu.npic.sps.mapper;

import edu.npic.sps.domain.Parking;
import edu.npic.sps.features.parking.dto.CreateParking;
import edu.npic.sps.features.parking.dto.ParkingResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParkingMapper {

    Parking fromCreateParking(CreateParking createParking);

    ParkingResponse toParkingResponse(Parking parking);

}
