package edu.npic.sps.features.parking.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateParking(
        @NotBlank(message = "Parking name is required!")
        String parkingName,
        @NotNull(message = "Slot quantity is required!")
        @Min(value = 1, message = "Slot quantity must be at least 1")
        @Max(value = Integer.MAX_VALUE, message = "Slot quantity must be a valid integer")
        Integer slotQty,
        String latitude,
        String longitude
) {
}
