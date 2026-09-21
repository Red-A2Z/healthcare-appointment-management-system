package org.example.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;



@Getter
@Setter
@NoArgsConstructor
@Entity
public class DoctorAvailability {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;

    public DoctorAvailability(Long id, Doctor doctor, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.doctor = doctor;
        this.startTime = startTime;
        this.endTime = endTime;
    }



}
