package org.example.dto.AppointmentDTOs;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.AppointmentStatus;


import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentRequestDTO {



    @NotNull(message = "Doctor ID cannot be null")
    @Positive(message = "Doctor ID should be positive")
    private Long doctorId;

    @NotNull(message = "Patient ID cannot be null")
    @Positive(message = "Patient ID should be positive")
    private Long patientId;


    @NotNull(message = "Start time cannot be null")
    @FutureOrPresent(message = "Start time shouldn't be in the past")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @FutureOrPresent(message = "End time shouldn't be in the past")
    private LocalDateTime endTime;

    @NotNull(message = "Reason for visiting cannot be null, empty or blank")
    private String reasonForVisit;


    private AppointmentStatus appointmentStatus;



}
