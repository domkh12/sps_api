package edu.npic.sps.features.parking;

import edu.npic.sps.domain.Parking;
import edu.npic.sps.features.parking.dto.CreateParking;
import edu.npic.sps.features.parking.dto.ParkingResponse;
import edu.npic.sps.features.parkingslot.dto.ParkingSlotResponse;
import edu.npic.sps.mapper.ParkingMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private static final Logger log = LoggerFactory.getLogger(ParkingServiceImpl.class);
    private final ParkingRepository parkingRepository;
    private final ParkingMapper parkingMapper;
    private final SimpMessagingTemplate simpMessageTemplate;

    @Override
    public ParkingResponse findByUuid(String uuid) {
        Parking parking = parkingRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Parking not found!")
        );

        return parkingMapper.toParkingResponse(parking);
    }

    @Override
    public void createNew(CreateParking createParking) {

        if (parkingRepository.existsByParkingNameContainsIgnoreCase(createParking.parkingName())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Parking name already exist!"
            );
        }

        Parking parking = parkingMapper.fromCreateParking(createParking);
        parking.setUuid(UUID.randomUUID().toString());
        parking.setIsDeleted(false);
        parking.setCreatedAt(LocalDateTime.now());
        parking.setUpdatedAt(LocalDateTime.now());

        parkingRepository.save(parking);
    }

    @Override
    public Page<ParkingResponse> findByName(String name, int pageNo, int pageSize) {
        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must be greater than zero"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Parking> parkingNotDeleted = parkingRepository.findByParkingNameContainsIgnoreCaseAndIsDeletedFalse(name, pageRequest);
        return parkingNotDeleted.map(parkingMapper::toParkingResponse);
    }

    @Override
    public void delete(String uuid) {
        Parking parking = parkingRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking not found")
        );
        parking.setIsDeleted(true);
        parkingRepository.save(parking);
    }

    @Override
    public Page<ParkingResponse> findAll(int pageNo, int pageSize) {
        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must be greater than zero"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Parking> parkingNotDeleted = parkingRepository.findByIsDeletedFalse(pageRequest);
        return parkingNotDeleted.map(parkingMapper::toParkingResponse);
    }

    public void updateIsAvailable(String userId, ParkingSlotResponse parkingSlotResponse) {
        log.info("Sending WS update to user : {}", userId, parkingSlotResponse);
        simpMessageTemplate.convertAndSendToUser(
                userId,
                "/update",
                parkingSlotResponse
        );
    }
}
