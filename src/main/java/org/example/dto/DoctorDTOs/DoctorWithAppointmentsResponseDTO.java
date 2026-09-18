package org.example.dto.DoctorDTOs;


import lombok.Getter;
import lombok.Setter;
import org.example.entity.Appointment;

import java.util.List;

@Getter
@Setter
public class DoctorWithAppointmentsResponseDTO {


    private Long id;

    private String firstName;
    private String lastName;
    private String specialty;
    private String phoneNumber;
    private String email;
    private Boolean isActive;


    private List<Appointment> doctorAppointmentList;

}
