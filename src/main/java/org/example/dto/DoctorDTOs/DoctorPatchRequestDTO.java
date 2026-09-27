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
            @NotBlank(message = "First Name must not be null, empty or blank")
            String> firstName;


    private JsonNullable<
            @NotBlank(message = "Last Name must not be null, empty or blank")
            String> lastName;


    private JsonNullable<
            @NotBlank(message = "Speciality must not be null, empty or blank")
            String> specialty;


    private JsonNullable<
            @NotNull(message = "Phone number must not be null")
            @Pattern(regexp = "^\\+\\d{7,15}$", message = "Phone number must be valid")
            String> phoneNumber;


    private JsonNullable<
            @NotNull(message = "Email must not be null")
            @Email(message = "Email must be valid")
            String> email;


    private JsonNullable<
            @NotNull(message = "Status must not be null")
            Boolean> isActive;



}
