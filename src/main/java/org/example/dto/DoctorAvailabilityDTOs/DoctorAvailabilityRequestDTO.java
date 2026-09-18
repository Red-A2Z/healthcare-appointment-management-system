package org.example.dto.DoctorAvailabilityDTOs;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class DoctorAvailabilityRequestDTO {


    @NotNull(message = "Doctor ID cannot be null")
    @Positive(message = "Doctor ID should be positive")
    private Long doctorId;

    @NotNull(message = "Start time cannot be null")
    @FutureOrPresent(message = "Start time shouldn't be in the past")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @FutureOrPresent(message = "End time shouldn't be in the past")
    private LocalDateTime endTime;


}
