package edu.npic.sps.features.parkingslot;

import edu.npic.sps.domain.ParkingSlot;
import edu.npic.sps.domain.ParkingSlotDetail;
import edu.npic.sps.domain.Vehicle;
import edu.npic.sps.features.parking.ParkingService;
import edu.npic.sps.features.parking.ParkingServiceImpl;
import edu.npic.sps.features.parkingslot.dto.CreateCarParking;
import edu.npic.sps.features.parkingslot.dto.ParkingSlotDetailResponse;
import edu.npic.sps.features.parkingslot.dto.ParkingSlotResponse;
import edu.npic.sps.features.user.UserRepository;
import edu.npic.sps.features.vehicle.VehicleRepository;
import edu.npic.sps.mapper.ParkingSlotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSlotServiceImpl implements ParkingSlotService{

    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingSlotMapper parkingSlotMapper;
    private final ParkingSlotDetailRepository parkingSlotDetailRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingServiceImpl parkingServiceImpl;
    private final UserRepository userRepository;

    @Override
    public Page<ParkingSlotDetailResponse> findAll(int pageNo, int pageSize) {

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must be greater than zero"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ParkingSlotDetail> parkingSlotPage = parkingSlotDetailRepository.findAll(pageRequest);
        return parkingSlotPage.map(parkingSlotMapper::toParkingSlotDetailResponse);

    }

    @Override
    public void createCarParking(CreateCarParking createCarParking) {

        Vehicle vehicle = vehicleRepository.findByNumberPlate(createCarParking.plateNumber()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found")
        );

        ParkingSlot parkingSlot = parkingSlotRepository.findByAlias(createCarParking.alias_slot()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking slot not found")
        );

        if (!userRepository.existsByUuid(createCarParking.userUuid())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        parkingSlot.setIsAvailable(createCarParking.isAvailable());
        parkingSlotRepository.save(parkingSlot);

        ParkingSlotDetail parkingSlotDetail = parkingSlotMapper.fromCreateCarParking(createCarParking);
        parkingSlotDetail.setVehicle(vehicle);
        parkingSlotDetail.setParkingSlot(parkingSlot);
        parkingSlotDetail.setIsDeleted(false);
        parkingSlotDetail.setCreatedAt(LocalDateTime.now());

        parkingSlotDetailRepository.save(parkingSlotDetail);

        parkingServiceImpl.updateIsAvailable(
                createCarParking.userUuid(),
                ParkingSlotResponse.builder()
                        .uuid(parkingSlot.getUuid())
                        .slotName(parkingSlot.getSlotName())
                        .isAvailable(parkingSlot.getIsAvailable())
                        .build()
        );

    }

    @Override
    public ParkingSlotDetailResponse findByUuid(String uuid) {
        ParkingSlot parkingSlot = parkingSlotRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "ParkingSlot not found!"
                )
        );

        ParkingSlotDetail parkingSlotDetail = parkingSlotDetailRepository.findById(parkingSlot.getId()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "ParkingSlot not found!"
                )
        );

        return parkingSlotMapper.toParkingSlotDetailResponse(parkingSlotDetail);
    }
}
