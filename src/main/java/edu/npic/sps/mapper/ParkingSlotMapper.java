package edu.npic.sps.mapper;

import edu.npic.sps.domain.ParkingSlot;
import edu.npic.sps.domain.ParkingSlotDetail;
import edu.npic.sps.features.parkingslot.dto.CreateCarParking;
import edu.npic.sps.features.parkingslot.dto.ParkingSlotDetailResponse;
import edu.npic.sps.features.parkingslot.dto.ParkingSlotResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParkingSlotMapper {

    ParkingSlotDetailResponse toParkingSlotDetailResponse(ParkingSlotDetail parkingSlotDetail);

    ParkingSlotDetail fromCreateCarParking(CreateCarParking createCarParking);

}
