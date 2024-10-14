package edu.npic.sps.features.parking.dto;

import edu.npic.sps.features.parkingslot.dto.ParkingSlotResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record ParkingResponse(
        String uuid,
        String parkingName,
        Integer slotQty,
        String latitude,
        String longitude,
        String lastUpdate,
        List<ParkingSlotResponse> parkingSlots
) {
}
