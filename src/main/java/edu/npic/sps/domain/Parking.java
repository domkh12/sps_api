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
@Table(name = "parking")
public class Parking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String uuid;
    @Column(nullable = false, unique = true, length = 100)
    private String parkingName;
    @Column(nullable = false)
    private Integer slotQty;

    //optional
    private String latitude;
    private String longitude;

    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String createdBy;
    private Boolean isDeleted;

    // relationship
    @OneToMany(mappedBy = "parking")
    private List<ParkingSlot> parkingSlots;
}
