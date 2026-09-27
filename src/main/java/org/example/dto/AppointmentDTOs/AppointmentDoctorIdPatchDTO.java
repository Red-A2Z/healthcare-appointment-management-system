package org.example.dto.AppointmentDTOs;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentDoctorIdPatchDTO {

    @NotNull(message = "Doctor ID must not be null or not provided")
    @Positive(message = "Doctor ID must be positive")
    private Long doctorId;
}
