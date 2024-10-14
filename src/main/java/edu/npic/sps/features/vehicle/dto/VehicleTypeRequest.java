package edu.npic.sps.features.vehicle.dto;

import jakarta.validation.constraints.NotBlank;

public record VehicleTypeRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Alias is required")
        String alias
) {
}
