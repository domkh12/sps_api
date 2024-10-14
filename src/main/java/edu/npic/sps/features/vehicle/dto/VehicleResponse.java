package edu.npic.sps.features.vehicle.dto;

import edu.npic.sps.features.user.dto.UserResponse;

import java.time.LocalDateTime;

public record VehicleResponse(

        String uuid,
        String numberPlate,
        String vehicleModel,
        String vehicleDescription,
        UserResponse user,
        LocalDateTime createdAt

) {
}
