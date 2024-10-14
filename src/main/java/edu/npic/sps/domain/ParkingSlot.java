package edu.npic.sps.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "parking-slots")
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String uuid;
    @Column(nullable = false, unique = true, length = 50)
    private String slotName;
    @Column(nullable = false, unique = true, length = 50)
    private String alias;
    @Column(nullable = false)
    private Boolean isAvailable;
    private Boolean isDeleted;
    private String createdBy;

    @OneToMany(mappedBy = "parkingSlot")
    private List<ParkingSlotDetail> parkingSlotDetail;

    @ManyToOne
    private Parking parking;
}
