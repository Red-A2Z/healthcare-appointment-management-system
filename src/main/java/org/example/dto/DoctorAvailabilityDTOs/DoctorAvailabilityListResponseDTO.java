package org.example.dto.DoctorAvailabilityDTOs;


import lombok.Getter;
import lombok.Setter;
import org.example.entity.DoctorAvailability;

import java.util.List;

@Getter
@Setter
public class DoctorAvailabilityListResponseDTO {


    private Long doctorId;
    private boolean isDoctorActive;
    private List<DoctorAvailability> doctorAvailabilityList;
}
