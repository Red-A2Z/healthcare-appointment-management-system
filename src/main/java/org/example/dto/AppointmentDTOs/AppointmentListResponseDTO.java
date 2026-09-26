package org.example.dto.AppointmentDTOs;



import lombok.Getter;
import lombok.Setter;
import org.example.entity.Appointment;
import org.example.enums.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AppointmentListResponseDTO {


    private Long doctorId;
    private Long patientId;
    private LocalDate startDate;
    private LocalDate endDate;
    private AppointmentStatus appointmentStatus;
    private List<Appointment> appointmentList;



}
