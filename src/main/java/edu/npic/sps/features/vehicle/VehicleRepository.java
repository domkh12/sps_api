package edu.npic.sps.features.vehicle;

import edu.npic.sps.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    @Query("select v from Vehicle v where upper(v.numberPlate) like upper(concat('%', ?1, '%'))")
    Optional<Vehicle> findByNumberPlate(String numberPlate);

}
