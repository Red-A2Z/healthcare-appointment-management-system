package org.example.dto.AppointmentDTOs;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentDoctorIdPatchDTO {

    @NotNull(message = "Doctor ID cannot be null")
    @Positive(message = "Doctor ID should be positive")
    private Long doctorId;
}
