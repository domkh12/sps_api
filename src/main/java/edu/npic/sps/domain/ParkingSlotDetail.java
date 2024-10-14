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
@Table(name = "parking-slot-details")
public class ParkingSlotDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    @Column(nullable = false, length = 100)
    private LocalDateTime timeIn;
    @Column(length = 100)
    private LocalDateTime timeOut;

    // relationship
    @ManyToOne
    private Vehicle vehicle;
    @ManyToOne
    private ParkingSlot parkingSlot;

}
