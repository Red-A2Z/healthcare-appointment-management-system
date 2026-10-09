package org.example.service;

import org.example.dto.AppointmentDTOs.AppointmentResponseDTO;
import org.example.entity.Appointment;
import org.example.entity.DoctorAvailability;
import org.example.exception.ConflictExcpetions.children.DoctorUnavailableException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentHelperService {



    public DoctorAvailability checkAppointmentAgainstDAs(List<DoctorAvailability> doctorAvailabilityList, Appointment newAppointment){

        Long doctorId = newAppointment.getDoctor().getId();
        LocalDateTime newAppointmentStartTime = newAppointment.getStartTime();
        LocalDateTime newAppointmentEndTime = newAppointment.getEndTime();


        int doctorAvailabilitiesCounter = 0;

        for(DoctorAvailability dav:doctorAvailabilityList){

            LocalDateTime davStartTime = dav.getStartTime();
            LocalDateTime davEndTime = dav.getEndTime();

            if(!newAppointmentStartTime.isBefore(davStartTime) && !newAppointmentEndTime.isAfter(davEndTime)){
                return dav;
            }else{
                doctorAvailabilitiesCounter++;
            }
        }

        if(doctorAvailabilitiesCounter==doctorAvailabilityList.size()){
            throw new DoctorUnavailableException("Doctor with id "+doctorId+" has no corresponding availability");
        }

        return null;
    }





    public AppointmentResponseDTO mapToAppointmentResponseDTO(Appointment appointment){

        AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO();

        appointmentResponseDTO.setId(appointment.getId());
        appointmentResponseDTO.setDoctorId(appointment.getDoctor().getId());
        appointmentResponseDTO.setPatientId(appointment.getPatient().getId());
        appointmentResponseDTO.setStartTime(appointment.getStartTime());
        appointmentResponseDTO.setEndTime(appointment.getEndTime());
        appointmentResponseDTO.setReasonForVisit(appointment.getReasonForVisit());
        appointmentResponseDTO.setAppointmentStatus(appointment.getAppointmentStatus());
        appointmentResponseDTO.setCreatedAt(appointment.getCreatedAt());

        return appointmentResponseDTO;

    }



}
