package org.example.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityListResponseDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityPatchRequestDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityRequestDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityResponseDTO;
import org.example.exception.InvalidDateRangeException;
import org.example.service.DoctorAvailabilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/doctoravailability")
@Validated
public class DoctorAvailabilityController {

    DoctorAvailabilityService doctorAvailabilityService;

    public DoctorAvailabilityController(DoctorAvailabilityService doctorAvailabilityService){
        this.doctorAvailabilityService = doctorAvailabilityService;

    }


    @PostMapping
    public ResponseEntity<DoctorAvailabilityResponseDTO> createDoctorAvailability(@Valid @RequestBody DoctorAvailabilityRequestDTO doctorAvailabilityRequestDTO){


        if(!doctorAvailabilityRequestDTO.getStartTime().isBefore(doctorAvailabilityRequestDTO.getEndTime())){
            throw new InvalidDateRangeException("Start time must be before end time");

        }

        DoctorAvailabilityResponseDTO doctorAvailabilityResponseDTO = doctorAvailabilityService.createDoctorAvailability(doctorAvailabilityRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(doctorAvailabilityResponseDTO);


    }


    @GetMapping
    public ResponseEntity<DoctorAvailabilityResponseDTO> readDoctorAvailability(@RequestParam
                                                                                    @NotNull(message = "DoctorAvailability ID must not be null or not provided")
                                                                                    @Positive(message = "DoctorAvailability ID must be positive")
                                                                                    Long id){

        return ResponseEntity.ok(doctorAvailabilityService.readDoctorAvailability(id));
    }


    @GetMapping("/list")
    public ResponseEntity<DoctorAvailabilityListResponseDTO> readAllDoctorAvailabilities(@RequestParam
                                                                                             @NotNull(message = "Doctor ID must not be null or not provided")
                                                                                             @Positive(message = "Doctor ID must be positive")
                                                                                             Long doctorId){

        return ResponseEntity.ok(doctorAvailabilityService.readAllDoctorAvailabilities(doctorId));
    }




    @PutMapping("/{id}")
    public ResponseEntity<DoctorAvailabilityResponseDTO> updateDoctorAvailability(@PathVariable
                                                                                      @NotNull(message = "DoctorAvailability ID must not be null or not provided")
                                                                                      @Positive(message = "DoctorAvailability ID must be positive")
                                                                                      Long id,
                                                                                  @Valid @RequestBody DoctorAvailabilityRequestDTO doctorAvailabilityRequestDTO){


        if(!doctorAvailabilityRequestDTO.getStartTime().isBefore(doctorAvailabilityRequestDTO.getEndTime())){
            throw new InvalidDateRangeException("Start time must be before end time");

        }


        return ResponseEntity.ok(doctorAvailabilityService.updateDoctorAvailability(id,doctorAvailabilityRequestDTO));

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctorAvailability(@PathVariable
                                                               @NotNull(message = "DoctorAvailability ID must not be null or not provided")
                                                               @Positive(message = "DoctorAvailability ID must be positive")
                                                               Long id){

        doctorAvailabilityService.deleteDoctorAvailability(id);

        return ResponseEntity.noContent().build();

    }


    @PatchMapping("/{id}")
    public ResponseEntity<DoctorAvailabilityResponseDTO> updateSomeFieldsForDoctorAvailability(@PathVariable
                                                                                                   @NotNull(message = "DoctorAvailability ID must not be null or not provided")
                                                                                                   @Positive(message = "DoctorAvailability ID must be positive")
                                                                                                   Long id,
                                                                                              @Valid @RequestBody DoctorAvailabilityPatchRequestDTO doctorAvailabilityPatchRequestDTO){

        if(doctorAvailabilityPatchRequestDTO.getStartTime().isPresent()
                && doctorAvailabilityPatchRequestDTO.getEndTime().isPresent()){

            LocalDateTime startTime = doctorAvailabilityPatchRequestDTO.getStartTime().get();
            LocalDateTime endTime = doctorAvailabilityPatchRequestDTO.getEndTime().get();
            if(!startTime.isBefore(endTime)){
                throw new InvalidDateRangeException("Start time must be before end time");
            }

        }


        return ResponseEntity.ok(doctorAvailabilityService.updateSomeFieldsForDoctorAvailability(id,doctorAvailabilityPatchRequestDTO));
    }






}
