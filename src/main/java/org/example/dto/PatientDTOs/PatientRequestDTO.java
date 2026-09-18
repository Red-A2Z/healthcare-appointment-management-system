package org.example.dto.PatientDTOs;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatientRequestDTO {


    @NotBlank(message = "First Name cannot be null, empty or blank")
    private String firstName;

    @NotBlank(message = "Last Name cannot be null, empty or blank")
    private String lastName;

    @PastOrPresent(message= "The date of birth should be today or in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+\\d{7,15}$", message = "Phone number should be valid")
    private String phoneNumber;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;






}
