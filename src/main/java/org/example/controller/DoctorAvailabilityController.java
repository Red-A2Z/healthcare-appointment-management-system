package org.example.controller;


import jakarta.validation.Valid;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityPatchRequestDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityRequestDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityResponseDTO;
import org.example.service.DoctorAvailabilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctoravailability")
public class DoctorAvailabilityController {

    DoctorAvailabilityService doctorAvailabilityService;

    public DoctorAvailabilityController(DoctorAvailabilityService doctorAvailabilityService){
        this.doctorAvailabilityService = doctorAvailabilityService;

    }


    @PostMapping
    public ResponseEntity<DoctorAvailabilityResponseDTO> createDoctorAvailability(@Valid @RequestBody DoctorAvailabilityRequestDTO doctorAvailabilityRequestDTO){

        DoctorAvailabilityResponseDTO doctorAvailabilityResponseDTO = doctorAvailabilityService.createDoctorAvailability(doctorAvailabilityRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(doctorAvailabilityResponseDTO);


    }


    @GetMapping
    public ResponseEntity<DoctorAvailabilityResponseDTO> readDoctorAvailability(@RequestParam Long id){

        return ResponseEntity.ok(doctorAvailabilityService.readDoctorAvailability(id));
    }




    @PutMapping("/{id}")
    public ResponseEntity<DoctorAvailabilityResponseDTO> updateDoctorAvailability(@PathVariable Long id,
                                                                                  @Valid @RequestBody DoctorAvailabilityRequestDTO doctorAvailabilityRequestDTO){

        return ResponseEntity.ok(doctorAvailabilityService.updateDoctorAvailability(id,doctorAvailabilityRequestDTO));

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctorAvailability(@PathVariable Long id){

        doctorAvailabilityService.deleteDoctorAvailability(id);

        return ResponseEntity.noContent().build();

    }


    @PatchMapping("/{id}")
    public ResponseEntity<DoctorAvailabilityResponseDTO> updateSomeFieldsForDoctorAvailability(@PathVariable Long id,
                                                                                              @Valid @RequestBody DoctorAvailabilityPatchRequestDTO doctorAvailabilityPatchRequestDTO){


        return ResponseEntity.ok(doctorAvailabilityService.updateSomeFieldsForDoctorAvailability(id,doctorAvailabilityPatchRequestDTO));
    }






}
