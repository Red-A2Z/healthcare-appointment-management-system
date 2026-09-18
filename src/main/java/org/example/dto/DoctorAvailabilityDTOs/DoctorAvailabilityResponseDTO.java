package org.example.dto.DoctorAvailabilityDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class DoctorAvailabilityResponseDTO {


    private Long id;

    private Long doctorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Instant createdAt;


}
