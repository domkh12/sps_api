package edu.npic.sps.features.parking;

import edu.npic.sps.features.parking.dto.CreateParking;
import edu.npic.sps.features.parking.dto.ParkingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{uuid}")
    ParkingResponse findByUuid(@PathVariable String uuid) {
        return parkingService.findByUuid(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    void createNew(@Valid @RequestBody CreateParking createParking) {
        parkingService.createNew(createParking);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    Page<ParkingResponse> findAll(@RequestParam(required = false, defaultValue = "1") int pageNo,
                                  @RequestParam(required = false, defaultValue = "30") int pageSize){
        return parkingService.findAll(pageNo, pageSize);

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    void delete(@PathVariable String uuid){
        parkingService.delete(uuid);
    }
}
