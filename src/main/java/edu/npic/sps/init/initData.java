package edu.npic.sps.init;

import edu.npic.sps.domain.*;
import edu.npic.sps.features.parking.ParkingRepository;
import edu.npic.sps.features.parkingslot.ParkingSlotDetailRepository;
import edu.npic.sps.features.parkingslot.ParkingSlotRepository;
import edu.npic.sps.features.user.RoleRepository;
import edu.npic.sps.features.user.UserRepository;
import edu.npic.sps.features.vehicle.VehicleRepository;
import edu.npic.sps.features.vehicle.VehicleTypeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class initData {

    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleRepository vehicleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ParkingRepository parkingRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingSlotDetailRepository parkingSlotDetailRepository;

    @PostConstruct
    public void init() {
        initVehicleTypeData();
        initRoles();
        initUsersData();
        initParking();
        initVehicleData();
        initParkingSlotsData();
        initParkingSlotDetailData();

    }

    private void initParkingSlotDetailData(){
        ParkingSlotDetail psd = new ParkingSlotDetail();
        psd.setParkingSlot(parkingSlotRepository.findById(1).get());
        psd.setVehicle(vehicleRepository.findById(1).get());
        psd.setTimeIn(LocalDateTime.now());
        psd.setTimeOut(LocalDateTime.now().plusHours(5));

        ParkingSlotDetail psd2 = new ParkingSlotDetail();
        psd.setParkingSlot(parkingSlotRepository.findById(2).get());
        psd.setVehicle(vehicleRepository.findById(2).get());
        psd.setTimeIn(LocalDateTime.now());
        psd.setTimeOut(LocalDateTime.now().plusHours(5));
        parkingSlotDetailRepository.save(psd);
    }

    private void initParkingSlotsData(){
        List<ParkingSlot> parkingSlots = new ArrayList<>();

        for (int parkingId = 1; parkingId <= 5; parkingId++) {
            Parking parking = parkingRepository.findById(parkingId).orElseThrow(() -> new RuntimeException("Parking not found"));

            for (int i = 1; i <= 10; i++) {
                ParkingSlot ps = new ParkingSlot();
                ps.setUuid(UUID.randomUUID().toString());
                ps.setSlotName("P" + parkingId + "-" + String.format("%02d", i));
                ps.setAlias("p" + parkingId + "-" + String.format("%02d", i));
                ps.setIsAvailable(true);
                ps.setIsDeleted(false);
                ps.setParking(parking);
                parkingSlots.add(ps);
            }
        }

        parkingSlotRepository.saveAll(parkingSlots);

    }

    private void initParking(){
        int batchSize = 1000;
        int totalRecords = 5;
        for (int i = 0; i < totalRecords; i += batchSize) {
            List<Parking> parkingBatch = new ArrayList<>(batchSize);
            for (int j = 0; j < batchSize && (i + j) < totalRecords; j++) {
                int parkingNumber = i + j + 1;
                Parking p = new Parking();
                p.setUuid(UUID.randomUUID().toString());
                p.setParkingName("P" + parkingNumber);
                p.setSlotQty(30 + (parkingNumber * 5));
                p.setLatitude(String.format("%.6f", 11.598035 + (parkingNumber * 0.001)));
                p.setLongitude(String.format("%.6f", 104.801771 + (parkingNumber * 0.001)));
                p.setCreatedAt(LocalDateTime.now());
                p.setUpdatedAt(LocalDateTime.now());
                p.setIsDeleted(false);
                parkingBatch.add(p);
            }
            parkingRepository.saveAll(parkingBatch);
        }
    }

    private void initUsersData(){

        User user = new User();
        user.setFullName("NPIC");
        user.setPassword(passwordEncoder.encode("Npic@2024"));
        user.setEmail("npic@gmail.com");
        user.setPhoneNumber("0877345470");
        user.setUuid(UUID.randomUUID().toString());
        user.setCreatedAt(LocalDateTime.now());
        user.setIsVerified(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsDeleted(false);

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(1).orElseThrow());
        roles.add(roleRepository.findById(2).orElseThrow());
        roles.add(roleRepository.findById(3).orElseThrow());

        user.setRoles(roles);

        userRepository.save(user);
    }

    private void initRoles(){
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().name("ADMIN").build());
        roles.add(Role.builder().name("STAFF").build());
        roles.add(Role.builder().name("USER").build());

        roleRepository.saveAll(roles);

    }

    private void initVehicleTypeData(){
        VehicleType vehicleType1 = new VehicleType();
        vehicleType1.setId(1);
        vehicleType1.setName("Suv");
        vehicleType1.setAlias("suv");
        VehicleType vehicleType2 = new VehicleType();
        vehicleType2.setId(2);
        vehicleType2.setName("Sports Car");
        vehicleType2.setAlias("sports-car");
        VehicleType vehicleType3 = new VehicleType();
        vehicleType3.setId(3);
        vehicleType3.setName("Hybrid Cars");
        vehicleType3.setAlias("hybrid-cars");
        VehicleType vehicleType4 = new VehicleType();
        vehicleType4.setId(4);
        vehicleType4.setName("Electric Cars");
        vehicleType4.setAlias("electric-cars");
        VehicleType vehicleType5 = new VehicleType();
        vehicleType5.setId(5);
        vehicleType5.setName("Trucks");
        vehicleType5.setAlias("trucks");

        vehicleTypeRepository.saveAll(List.of(vehicleType1, vehicleType2, vehicleType3, vehicleType4, vehicleType5));


    }

    private void initVehicleData(){
        VehicleType vehicleType = vehicleTypeRepository.findById(1).orElseThrow();
        Vehicle v1 = new Vehicle();
        v1.setId(1);
        v1.setUuid(UUID.randomUUID().toString());
        v1.setVehicleModel("Lexus");
        v1.setNumberPlate("1AF-0022");
        v1.setVehicleType(vehicleType);
        v1.setIsDeleted(false);
        v1.setCreatedAt(LocalDateTime.now());
        v1.setVehicleDescription("This is car 1");
        v1.setUser(userRepository.findById(1).get());

        Vehicle v2 = new Vehicle();
        v2.setId(2);
        v2.setUuid(UUID.randomUUID().toString());
        v2.setVehicleModel("Camry");
        v2.setNumberPlate("1AF-0322");
        v2.setVehicleType(vehicleType);
        v2.setIsDeleted(false);
        v2.setVehicleDescription("This is car 2");
        v2.setCreatedAt(LocalDateTime.now());
        v2.setUser(userRepository.findById(1).get());

        vehicleRepository.saveAll(List.of(v1, v2));

    }

}
