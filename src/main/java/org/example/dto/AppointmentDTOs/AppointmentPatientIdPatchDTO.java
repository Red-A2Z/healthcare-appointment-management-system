package org.example.dto.AppointmentDTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentPatientIdPatchDTO {

    @NotNull(message = "Patient ID must not be null or not provided")
    @Positive(message = "Patient ID must be positive")
    private Long patientId;
}
