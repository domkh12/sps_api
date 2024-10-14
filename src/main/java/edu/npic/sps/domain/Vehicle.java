package edu.npic.sps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String uuid;
    @Column(unique = true, length = 50)
    private String numberPlate;

    // optional
    private String color;
    private String vehicleModel;
    private String vehicleDescription;

    private LocalDateTime createdAt;
    private Boolean isDeleted;

    // relationship
    @OneToMany(mappedBy = "vehicle")
    private List<ParkingSlotDetail> parkingSlotDetail;

    @ManyToOne
    private VehicleType vehicleType;

    @ManyToOne
    private User user;

}
