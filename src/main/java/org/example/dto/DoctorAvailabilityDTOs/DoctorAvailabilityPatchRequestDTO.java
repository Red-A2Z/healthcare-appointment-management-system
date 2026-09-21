package org.example.dto.DoctorAvailabilityDTOs;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
public class DoctorAvailabilityPatchRequestDTO {



    private JsonNullable<
            @NotNull(message = "Doctor ID cannot be null")
            @Positive(message = "Doctor ID should be positive")
                    Long> doctorId;

    private JsonNullable<
            @NotNull(message = "Start time cannot be null")
            @FutureOrPresent(message = "Start time shouldn't be in the past")
                    LocalDateTime> startTime;

    private JsonNullable<
            @NotNull(message = "End time cannot be null")
            @FutureOrPresent(message = "End time shouldn't be in the past")
                    LocalDateTime> endTime;



}
