package org.example.dto.AppointmentDTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentPatientIdPatchDTO {

    @NotNull(message = "Patient ID cannot be null")
    @Positive(message = "Patient ID should be positive")
    private Long patientId;
}
