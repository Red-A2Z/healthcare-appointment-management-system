package org.example.dto.DoctorDTOs;


import lombok.Getter;
import lombok.Setter;
import org.example.entity.DoctorAvailability;

import java.time.Instant;
import java.util.List;



@Getter
@Setter
public class DoctorResponseDTO {


    private Long id;

    private String firstName;
    private String lastName;
    private String specialty;
    private String phoneNumber;
    private String email;
    private Boolean isActive;
    private Instant createdAt;

    private List<DoctorAvailability> doctorAvailabilityList;



}
