package org.example.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
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



    public Doctor(Long id, String firstName, String lastName, String specialty, String phoneNumber, String email, Boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isActive = isActive;
    }








}
