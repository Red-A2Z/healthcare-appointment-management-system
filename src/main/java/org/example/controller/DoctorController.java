package org.example.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.dto.DoctorDTOs.DoctorPatchRequestDTO;
import org.example.dto.DoctorDTOs.DoctorRequestDTO;
import org.example.dto.DoctorDTOs.DoctorResponseDTO;
import org.example.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
@Validated
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService){
        this.doctorService = doctorService;
    }


    @PostMapping
    public ResponseEntity<DoctorResponseDTO> createDoctor(@Valid @RequestBody DoctorRequestDTO doctorRequestDTO){

        DoctorResponseDTO doctorResponseDTO = doctorService.createDoctor(doctorRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(doctorResponseDTO);
    }


    @GetMapping
    public ResponseEntity<DoctorResponseDTO> readDoctor(@RequestParam
                                                            @NotNull(message = "Doctor ID must not be null or not provided")
                                                            @Positive(message = "Doctor ID must be positive")
                                                            Long id){

        return ResponseEntity.ok(doctorService.readDoctor(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable
                                                              @NotNull(message = "Doctor ID must not be null or not provided")
                                                              @Positive(message = "Doctor ID must be positive")
                                                              Long id,
                                                          @Valid @RequestBody DoctorRequestDTO doctorRequestDTO){


        return ResponseEntity.ok(doctorService.updateDoctor(id,doctorRequestDTO));

    }



    @PatchMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> updateSomeFieldsForDoctor(@PathVariable
                                                                           @NotNull(message = "Doctor ID must not be null or not provided")
                                                                           @Positive(message = "Doctor ID must be positive")
                                                                           Long id,
                                                          @Valid @RequestBody DoctorPatchRequestDTO doctorPatchRequestDTO){

        DoctorResponseDTO doctorResponseDTO = doctorService.updateSomeFieldsForDoctor(id,doctorPatchRequestDTO);

        return ResponseEntity.ok(doctorResponseDTO);

    }




}
