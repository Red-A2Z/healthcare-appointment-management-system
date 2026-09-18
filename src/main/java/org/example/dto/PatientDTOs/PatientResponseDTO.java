package org.example.dto.PatientDTOs;



import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;


@Getter
@Setter
public class PatientResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private Instant createdAt;

}
