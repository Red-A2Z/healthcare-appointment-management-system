package org.example.dto.PatientDTOs;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class PatientPatchRequestDTO {

    private JsonNullable<
            @NotBlank(message = "First Name cannot be null, empty or blank")
                    String> firstName;


    private JsonNullable<
            @NotBlank(message = "Last Name cannot be null, empty or blank")
                    String> lastName;


    private JsonNullable<
            @PastOrPresent(message= "The date of birth should be today or in the past")
                    LocalDate> dateOfBirth;


    private JsonNullable<
            @NotNull(message = "Phone number cannot be null")
            @Pattern(regexp = "^\\+\\d{7,15}$", message = "Phone number should be valid")
                    String> phoneNumber;



    private JsonNullable<
            @NotNull(message = "Email cannot be null")
            @Email(message = "Email should be valid")
                    String> email;

}
