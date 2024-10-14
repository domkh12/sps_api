package edu.npic.sps.features.parkingslot.dto;

import lombok.Builder;

@Builder
public record ParkingSlotResponse(

        String uuid,
        String slotName,
        Boolean isAvailable
        //Vehicle vehicle

) {
}
