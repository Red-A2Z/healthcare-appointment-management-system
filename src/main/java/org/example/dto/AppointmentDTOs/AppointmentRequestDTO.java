package org.example.dto.AppointmentDTOs;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentRequestDTO {



    @NotNull(message = "Doctor ID must not be null or not provided")
    @Positive(message = "Doctor ID must be positive")
    private Long doctorId;

    @NotNull(message = "Patient ID must not be null or not provided")
    @Positive(message = "Patient ID must be positive")
    private Long patientId;


    @NotNull(message = "Start time must not be null or not provided")
    @FutureOrPresent(message = "Start time must be in the past")
    private LocalDateTime startTime;

    @NotNull(message = "End time must not be null or not provided")
    @FutureOrPresent(message = "End time must be in the past")
    private LocalDateTime endTime;

    @NotBlank(message = "Reason for visiting must not be null or not provided")
    private String reasonForVisit;




}
