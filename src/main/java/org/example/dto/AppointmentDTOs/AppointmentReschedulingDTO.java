package org.example.dto.AppointmentDTOs;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentReschedulingDTO {

    @NotNull(message = "Start time cannot be null")
    @FutureOrPresent(message = "Start time shouldn't be in the past")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @FutureOrPresent(message = "End time shouldn't be in the past")
    private LocalDateTime endTime;


}
