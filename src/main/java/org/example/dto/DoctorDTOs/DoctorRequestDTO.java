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

    @NotBlank(message = "First Name cannot be not provided, null, empty or blank")
    private String firstName;

    @NotBlank(message = "Last Name cannot be not provided, null, empty or blank")
    private String lastName;

    @NotBlank(message = "Speciality cannot be not provided, null, empty or blank")
    private String specialty;

    @NotNull(message = "Phone number cannot be not provided or null")
    @Pattern(regexp = "^\\+\\d{7,15}$", message = "Phone number should be valid")
    private String phoneNumber;

    @NotNull(message = "Email cannot be not provided or null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Status cannot be not provided or null")
    private Boolean isActive;



}
