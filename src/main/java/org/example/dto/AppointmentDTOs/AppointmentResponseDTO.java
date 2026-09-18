package org.example.dto.AppointmentDTOs;


import lombok.Getter;
import lombok.Setter;
import org.example.enums.AppointmentStatus;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentResponseDTO {

    private Long id;

    private Long doctorId;

    private Long patientId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reasonForVisit;

    private AppointmentStatus appointmentStatus;

    private Instant createdAt;




}
