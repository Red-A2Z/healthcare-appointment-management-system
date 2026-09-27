package org.example.dto.PatientDTOs;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatientRequestDTO {


    @NotBlank(message = "First Name must not be null, empty, blank or not provided")
    private String firstName;

    @NotBlank(message = "Last Name must not be null, empty, blank or not provided")
    private String lastName;

    @PastOrPresent(message= "The date of birth must be today or in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Phone number must not be null or not provided")
    @Pattern(regexp = "^\\+\\d{7,15}$", message = "Phone number must be valid")
    private String phoneNumber;

    @NotNull(message = "Email must not be null or not provided")
    @Email(message = "Email must be valid")
    private String email;






}
