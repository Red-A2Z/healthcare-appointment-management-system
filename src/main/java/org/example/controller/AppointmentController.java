package org.example.controller;


import jakarta.validation.Valid;
import org.example.dto.AppointmentDTOs.*;
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




    @PatchMapping("/changedoctor/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateDoctorIdForAppointment(@PathVariable Long id,
                                                                               @Valid @RequestBody AppointmentDoctorIdPatchDTO appointmentDoctorIdPatchDTO){

        return ResponseEntity.ok(appointmentService.updateDoctorIdForAppointment(id,appointmentDoctorIdPatchDTO));
    }


    @PatchMapping("/changepatient/{id}")
    public ResponseEntity<AppointmentResponseDTO> updatePatientIdForAppointment(@PathVariable Long id,
                                                                               @Valid @RequestBody AppointmentPatientIdPatchDTO appointmentPatientIdPatchDTO){

        return ResponseEntity.ok(appointmentService.updatePatientIdForAppointment(id,appointmentPatientIdPatchDTO));
    }


    @PatchMapping("/reschedule/{id}")
    public ResponseEntity<AppointmentResponseDTO> rescheduleAppointment(@PathVariable Long id,
                                                                                @Valid @RequestBody AppointmentReschedulingDTO appointmentReschedulingDTO){

        if(!appointmentReschedulingDTO.getStartTime().isBefore(appointmentReschedulingDTO.getEndTime())){
            throw new InvalidDateRangeException("Start time should be before end time");
        }

        return ResponseEntity.ok(appointmentService.rescheduleAppointment(id,appointmentReschedulingDTO));
    }

    @PatchMapping("/updaterfv/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateRFV(@PathVariable Long id,
                                                            @Valid @RequestBody AppointmentRFVPatchDTO appointmentRFVPatchDTO){

        return ResponseEntity.ok(appointmentService.updateRFV(id,appointmentRFVPatchDTO));
    }









    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id){

        return null;
    }





}
