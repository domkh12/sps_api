package edu.npic.sps.features.vehicle;

import edu.npic.sps.features.vehicle.dto.CreateVehicleType;
import edu.npic.sps.features.vehicle.dto.VehicleTypeRequest;
import edu.npic.sps.features.vehicle.dto.VehicleTypeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicle-types")
@RequiredArgsConstructor
public class VehicleTypeController {

    private final VehicleTypeService vehicleTypeService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{alias}")
    void deleteByAlias(@PathVariable String alias) {
        vehicleTypeService.deleteByAlias(alias);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    List<VehicleTypeResponse> findAll() {
        return vehicleTypeService.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void createNew(@Valid @RequestBody CreateVehicleType createVehicleType){
        vehicleTypeService.createNew(createVehicleType);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{alias}")
    VehicleTypeResponse updateByAlias(@PathVariable String alias,
                       @Valid @RequestBody VehicleTypeRequest vehicleTypeRequest){
        return vehicleTypeService.updateByAlias(alias, vehicleTypeRequest);
    }

}
