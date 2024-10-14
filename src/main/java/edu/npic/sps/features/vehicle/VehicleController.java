package edu.npic.sps.features.vehicle;

import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;


    @GetMapping
    Page<VehicleResponse> findAll(@RequestParam(required = false,defaultValue = "1") int pageNo,
                                     @RequestParam(required = false,defaultValue = "20") int pageSize){
        return vehicleService.findAll(pageNo, pageSize);
    }

    @GetMapping("{numberPlate}")
    VehicleResponse findByNumPlate(@PathVariable String numberPlate){
        return vehicleService.findByNumPlate(numberPlate);
    }

}
