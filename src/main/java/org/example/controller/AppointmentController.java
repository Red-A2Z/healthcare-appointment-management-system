package org.example.controller;


import jakarta.validation.Valid;
import org.example.dto.AppointmentDTOs.AppointmentRequestDTO;
import org.example.dto.AppointmentDTOs.AppointmentResponseDTO;
import org.example.exception.InvalidDateRangeException;
import org.example.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }




    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@Valid @RequestBody AppointmentRequestDTO appointmentRequestDTO){

        if(appointmentRequestDTO.getStartTime().isAfter(appointmentRequestDTO.getEndTime())
        || appointmentRequestDTO.getStartTime().isEqual(appointmentRequestDTO.getEndTime())){

            throw new InvalidDateRangeException("Start time shouldn't be after end time");

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.createAppointment(appointmentRequestDTO));
    }




    @GetMapping
    public ResponseEntity<AppointmentResponseDTO> readAppointment(@RequestParam Long id){

        return null;
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id){

        return null;
    }





}
