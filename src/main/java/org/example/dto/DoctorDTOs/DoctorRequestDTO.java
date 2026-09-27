package org.example.dto.DoctorDTOs;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class DoctorRequestDTO {

    @NotBlank(message = "First Name must not be null, empty, blank or not provided")
    private String firstName;

    @NotBlank(message = "Last Name must not be null, empty, blank or not provided")
    private String lastName;

    @NotBlank(message = "Speciality must not be null, empty, blank or not provided")
    private String specialty;

    @NotNull(message = "Phone number must not be null or not provided")
    @Pattern(regexp = "^\\+\\d{7,15}$", message = "Phone number must be valid")
    private String phoneNumber;

    @NotNull(message = "Email must not be not provided or null")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Status must not be null or not provided")
    private Boolean isActive;



}
