package org.example.dto.AppointmentDTOs;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.AppointmentStatus;

@Getter
@Setter
public class AppointmentStatusRequestDTO {

    private AppointmentStatus appointmentStatus;
}
