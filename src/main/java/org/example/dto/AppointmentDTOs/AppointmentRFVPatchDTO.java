package org.example.dto.AppointmentDTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppointmentRFVPatchDTO {

    @NotBlank(message = "Reason for visiting must not be null, empty, blank or not provided")
    private String reasonForVisit;
}
