package org.example.dto.AppointmentDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.AppointmentStatus;

@Getter
@Setter
public class AppointmentStatusRequestDTO {

    @NotNull(message = "Appointment Status cannot be null")
    private AppointmentStatus appointmentStatus;
}
