package org.example.controller;


import jakarta.validation.Valid;
import org.example.dto.AppointmentDTOs.*;
import org.example.exception.InvalidDateRangeException;
import org.example.service.AppointmentService.AppointmentMaintenanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    AppointmentMaintenanceService appointmentMaintenanceService;

    public AppointmentController(AppointmentMaintenanceService appointmentMaintenanceService) {
        this.appointmentMaintenanceService = appointmentMaintenanceService;
    }




    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@Valid @RequestBody AppointmentRequestDTO appointmentRequestDTO){

        if(appointmentRequestDTO.getStartTime().isAfter(appointmentRequestDTO.getEndTime())
        || appointmentRequestDTO.getStartTime().isEqual(appointmentRequestDTO.getEndTime())){

            throw new InvalidDateRangeException("Start time shouldn't be after end time");

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentMaintenanceService.createAppointment(appointmentRequestDTO));
    }




    @GetMapping
    public ResponseEntity<AppointmentResponseDTO> readAppointment(@RequestParam Long id){

        return null;
    }




    @PatchMapping("/doctor/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateDoctorIdForAppointment(@PathVariable Long id,
                                                                               @Valid @RequestBody AppointmentDoctorIdPatchDTO appointmentDoctorIdPatchDTO){

        return ResponseEntity.ok(appointmentMaintenanceService.updateDoctorIdForAppointment(id,appointmentDoctorIdPatchDTO));
    }


    @PatchMapping("/patient/{id}")
    public ResponseEntity<AppointmentResponseDTO> updatePatientIdForAppointment(@PathVariable Long id,
                                                                               @Valid @RequestBody AppointmentPatientIdPatchDTO appointmentPatientIdPatchDTO){

        return ResponseEntity.ok(appointmentMaintenanceService.updatePatientIdForAppointment(id,appointmentPatientIdPatchDTO));
    }


    @PatchMapping("/reschedule/{id}")
    public ResponseEntity<AppointmentResponseDTO> rescheduleAppointment(@PathVariable Long id,
                                                                                @Valid @RequestBody AppointmentReschedulingDTO appointmentReschedulingDTO){

        if(!appointmentReschedulingDTO.getStartTime().isBefore(appointmentReschedulingDTO.getEndTime())){
            throw new InvalidDateRangeException("Start time should be before end time");
        }

        return ResponseEntity.ok(appointmentMaintenanceService.rescheduleAppointment(id,appointmentReschedulingDTO));
    }

    @PatchMapping("/reasonforvisit/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateRFV(@PathVariable Long id,
                                                            @Valid @RequestBody AppointmentRFVPatchDTO appointmentRFVPatchDTO){

        return ResponseEntity.ok(appointmentMaintenanceService.updateRFV(id,appointmentRFVPatchDTO));
    }


    @PatchMapping("/status/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointmentStatus(@PathVariable Long id,
                                                                          @Valid @RequestBody AppointmentStatusRequestDTO appointmentStatusRequestDTO){


        return ResponseEntity.ok(appointmentMaintenanceService.updateAppointmentStatus(id, appointmentStatusRequestDTO));
    }







}
