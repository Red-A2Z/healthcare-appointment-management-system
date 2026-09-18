package org.example.dto.DoctorDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;




@Getter
@Setter
@AllArgsConstructor
public class DoctorPatchRequestDTO {


    private JsonNullable<
            @NotBlank(message = "First Name cannot be null, empty or blank")
            String> firstName;


    private JsonNullable<
            @NotBlank(message = "Last Name cannot be null, empty or blank")
            String> lastName;


    private JsonNullable<
            @NotBlank(message = "Speciality cannot be null, empty or blank")
            String> specialty;


    private JsonNullable<
            @NotNull(message = "Phone number cannot be null")
            @Pattern(regexp = "^\\+\\d{7,15}$", message = "Phone number should be valid")
            String> phoneNumber;


    private JsonNullable<
            @NotNull(message = "Email cannot be null")
            @Email(message = "Email should be valid")
            String> email;


    private JsonNullable<
            @NotNull(message = "Status cannot be null")
            Boolean> isActive;



}
