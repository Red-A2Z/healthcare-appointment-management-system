package org.example.controller;


import jakarta.validation.Valid;
import org.example.dto.DoctorDTOs.DoctorPatchRequestDTO;
import org.example.dto.DoctorDTOs.DoctorRequestDTO;
import org.example.dto.DoctorDTOs.DoctorResponseDTO;
import org.example.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
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
    public ResponseEntity<DoctorResponseDTO> readDoctor(@RequestParam Long id){

        DoctorResponseDTO doctorResponseDTO = doctorService.readDoctor(id);

        return ResponseEntity.ok(doctorResponseDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable Long id,
            @Valid @RequestBody DoctorRequestDTO doctorRequestDTO){

        DoctorResponseDTO doctorResponseDTO = doctorService.updateDoctor(id,doctorRequestDTO);

        return ResponseEntity.ok(doctorResponseDTO);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor( @PathVariable Long id){

        doctorService.deleteDoctor(id);

        return ResponseEntity.noContent().build();
    }



    @PatchMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> updateSomeFieldsForDoctor(@PathVariable Long id,
                                                          @Valid @RequestBody DoctorPatchRequestDTO doctorPatchRequestDTO){

        DoctorResponseDTO doctorResponseDTO = doctorService.updateSomeFieldsForDoctor(id,doctorPatchRequestDTO);

        return ResponseEntity.ok(doctorResponseDTO);

    }




}
