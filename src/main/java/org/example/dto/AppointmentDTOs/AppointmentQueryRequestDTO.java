package org.example.dto.AppointmentDTOs;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.AppointmentStatus;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Getter
@Setter
public class AppointmentQueryRequestDTO {


    private JsonNullable<
            @NotNull(message = "Doctor ID cannot be null")
            @Positive(message = "Doctor ID should be positive")
                    Long> doctorId;


    private JsonNullable<
            @NotNull(message = "Patient ID cannot be null")
            @Positive(message = "Patient ID should be positive")
                    Long> patientId;


    private JsonNullable<
            @NotNull(message = "Start date cannot be null")
                    LocalDate> startDate;


    private JsonNullable<
            @NotNull(message = "End date cannot be null")
                    LocalDate> endDate;


    private JsonNullable<
            @NotNull(message = "Appointment Status cannot be null")
                    AppointmentStatus> appointmentStatus;



}
