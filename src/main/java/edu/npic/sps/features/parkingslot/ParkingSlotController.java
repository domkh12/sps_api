package edu.npic.sps.features.parkingslot;

import edu.npic.sps.features.parkingslot.dto.CreateCarParking;
import edu.npic.sps.features.parkingslot.dto.ParkingSlotDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/parking-slots")
@RequiredArgsConstructor
public class ParkingSlotController {

    private final ParkingSlotService parkingSlotService;


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    Page<ParkingSlotDetailResponse> findAll(@RequestParam(required = false, defaultValue = "1") int pageNo,
                                            @RequestParam(required = false, defaultValue = "30") int pageSize) {
        return parkingSlotService.findAll(pageNo,pageSize);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{uuid}")
    ParkingSlotDetailResponse findByUuid(@PathVariable String uuid) {
        return parkingSlotService.findByUuid(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    void createCarParking(@RequestBody CreateCarParking createCarParking){
        parkingSlotService.createCarParking(createCarParking);
    }

}
