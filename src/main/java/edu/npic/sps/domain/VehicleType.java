package edu.npic.sps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "vehicle-types")
public class VehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false,length = 250)
    private String name;
    @Column(unique = true, nullable = false,length = 250)
    private String alias;

    // relationship
    @OneToMany(mappedBy = "vehicleType")
    private List<Vehicle> vehicle;
}
