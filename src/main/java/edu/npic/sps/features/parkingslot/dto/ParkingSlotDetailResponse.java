package edu.npic.sps.features.parkingslot.dto;

import edu.npic.sps.domain.ParkingSlot;
import edu.npic.sps.domain.Vehicle;
import edu.npic.sps.features.user.dto.UserResponse;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;

import java.time.LocalDateTime;

public record ParkingSlotDetailResponse(

        LocalDateTime timeIn,
        LocalDateTime timeOut,
        VehicleResponse vehicle,
        ParkingSlotResponse parkingSlot

) {
}
