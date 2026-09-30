package org.example.dto.DoctorAvailabilityDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DoctorAvailabilityResponseDTO {


    private Long id;

    private Long doctorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Instant createdAt;

    public DoctorAvailabilityResponseDTO(Long id, Long doctorId, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.doctorId = doctorId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
