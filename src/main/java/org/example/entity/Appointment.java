package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enums.AppointmentStatus;

import java.time.Instant;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;


    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reasonForVisit;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;


    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;




    public Appointment(Long id, Doctor doctor, Patient patient, LocalDateTime startTime, LocalDateTime endTime, String reasonForVisit, AppointmentStatus appointmentStatus) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reasonForVisit = reasonForVisit;
        this.appointmentStatus = appointmentStatus;
    }
}
