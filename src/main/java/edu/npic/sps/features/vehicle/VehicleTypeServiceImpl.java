package edu.npic.sps.features.vehicle;

import edu.npic.sps.domain.VehicleType;
import edu.npic.sps.features.vehicle.dto.CreateVehicleType;
import edu.npic.sps.features.vehicle.dto.VehicleTypeRequest;
import edu.npic.sps.features.vehicle.dto.VehicleTypeResponse;
import edu.npic.sps.mapper.VehicleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleTypeServiceImpl implements VehicleTypeService{

    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public void deleteByAlias(String alias) {
        VehicleType vehicleType = vehicleTypeRepository.findByAlias(alias).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Vehicle type alias not found!")
        );

        vehicleTypeRepository.delete(vehicleType);
    }

    @Override
    public VehicleTypeResponse updateByAlias(String alias, VehicleTypeRequest vehicleTypeRequest) {

        VehicleType vehicleType = vehicleTypeRepository.findByAlias(alias).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Vehicle type alias not found!")
        );

        vehicleType.setAlias(vehicleTypeRequest.alias());
        vehicleType.setName(vehicleTypeRequest.name());

        vehicleType = vehicleTypeRepository.save(vehicleType);

        return vehicleMapper.toVehicleTypeResponse(vehicleType);
    }

    @Override
    public List<VehicleTypeResponse> findAll() {
        List<VehicleType> vehicleTypeList = vehicleTypeRepository.findAll();
        return vehicleMapper.toVehicleTypeResponse(vehicleTypeList);
    }

    @Override
    public void createNew(CreateVehicleType createVehicleType) {

        if (vehicleTypeRepository.existsByAlias(createVehicleType.alias())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle type already exists!");
        }

        VehicleType vehicleType = new VehicleType();
        vehicleType.setAlias(createVehicleType.alias());
        vehicleType.setName(createVehicleType.name());
        vehicleType.setVehicle(new ArrayList<>());
        vehicleTypeRepository.save(vehicleType);
    }
}
