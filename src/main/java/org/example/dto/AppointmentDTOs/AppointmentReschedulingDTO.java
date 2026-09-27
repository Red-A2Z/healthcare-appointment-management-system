package org.example.dto.AppointmentDTOs;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentReschedulingDTO {

    @NotNull(message = "Start time must not be null or not provided")
    @FutureOrPresent(message = "Start time must not be in the past")
    private LocalDateTime startTime;

    @NotNull(message = "End time must not be null or not provided")
    @FutureOrPresent(message = "End time must not be in the past")
    private LocalDateTime endTime;


}
