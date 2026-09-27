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
            @NotBlank(message = "First Name must not be null, empty or blank")
                    String> firstName;


    private JsonNullable<
            @NotBlank(message = "Last Name must not be null, empty or blank")
                    String> lastName;


    private JsonNullable<
            @PastOrPresent(message= "The date of birth must be today or in the past")
                    LocalDate> dateOfBirth;


    private JsonNullable<
            @NotNull(message = "Phone number must not be null")
            @Pattern(regexp = "^\\+\\d{7,15}$", message = "Phone number must be valid")
                    String> phoneNumber;



    private JsonNullable<
            @NotNull(message = "Email must not be null")
            @Email(message = "Email must be valid")
                    String> email;

}
