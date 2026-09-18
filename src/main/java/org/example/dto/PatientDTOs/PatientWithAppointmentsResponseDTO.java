package org.example.dto.PatientDTOs;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.Appointment;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
public class PatientWithAppointmentsResponseDTO {


    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;

    private List<Appointment> patientAppointmentList;

}
