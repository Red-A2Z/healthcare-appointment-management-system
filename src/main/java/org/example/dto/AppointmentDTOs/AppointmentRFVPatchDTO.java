package org.example.dto.AppointmentDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppointmentRFVPatchDTO {

    @NotNull(message = "Reason for visiting cannot be null, empty or blank")
    private String reasonForVisit;
}
