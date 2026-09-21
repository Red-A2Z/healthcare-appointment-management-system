package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.PatientDTOs.PatientPatchRequestDTO;
import org.example.dto.PatientDTOs.PatientRequestDTO;
import org.example.dto.PatientDTOs.PatientResponseDTO;
import org.example.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
public class PatientController {


    PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }




    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO){

        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientRequestDTO));
    }



    @GetMapping
    public ResponseEntity<PatientResponseDTO> readPatient(@RequestParam Long id){

        return ResponseEntity.ok(patientService.readPatient(id));
    }



    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable Long id,
                                                            @Valid @RequestBody PatientRequestDTO patientRequestDTO){

        return ResponseEntity.ok(patientService.updatePatient(id,patientRequestDTO));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id){

        patientService.deletePatient(id);

        return ResponseEntity.noContent().build();
    }




    @PatchMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updateSomeFieldsForPatient(@PathVariable Long id,
                                                                         @Valid @RequestBody PatientPatchRequestDTO patientPatchRequestDTO){

        return ResponseEntity.ok(patientService.updateSomeFieldsForPatient(id,patientPatchRequestDTO));
    }



}
