package org.example.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String specialty;
    private String phoneNumber;
    private String email;
    private Boolean isActive;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;

    @OneToMany(mappedBy = "doctor")
    private List<DoctorAvailability> doctorAvailabilityList;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> doctorAppointmentList;









}
