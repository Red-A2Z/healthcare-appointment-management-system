package org.example.dto.DoctorAvailabilityDTOs;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
public class DoctorAvailabilityRequestDTO {


    @NotNull(message = "Doctor ID must not be null or not provided")
    @Positive(message = "Doctor ID must be positive")
    private Long doctorId;

    @NotNull(message = "Start time must not be null or not provided")
    @FutureOrPresent(message = "Start time must not be in the past")
    private LocalDateTime startTime;

    @NotNull(message = "End time must not be null or not provided")
    @FutureOrPresent(message = "End time must not be in the past")
    private LocalDateTime endTime;


}
