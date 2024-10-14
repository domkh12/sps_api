package edu.npic.sps.features.vehicle;

import edu.npic.sps.domain.Vehicle;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import edu.npic.sps.mapper.VehicleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService{

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public VehicleResponse findByNumPlate(String numberPlate) {

        Vehicle vehicle = vehicleRepository.findByNumberPlate(numberPlate).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "NumberPlate not found!"
                )
        );
        return vehicleMapper.toVehicleResponse(vehicle);
    }

    @Override
    public Page<VehicleResponse> findAll(int pageNo, int pageSize) {

        if (pageNo < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number must be greater than 0!"
            );
        }

        if (pageSize < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page size must be greater than 0!"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Vehicle> vehicles = vehicleRepository.findAll(pageRequest);

        return vehicles.map(vehicleMapper::toVehicleResponse);
    }
}
