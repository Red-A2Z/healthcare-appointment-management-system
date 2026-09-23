package org.example.service;


import jakarta.transaction.Transactional;
import org.example.dto.AppointmentDTOs.AppointmentRequestDTO;
import org.example.dto.AppointmentDTOs.AppointmentResponseDTO;
import org.example.entity.Appointment;
import org.example.entity.Doctor;
import org.example.entity.DoctorAvailability;
import org.example.entity.Patient;
import org.example.enums.AppointmentStatus;
import org.example.exception.DoctorInactiveException;
import org.example.exception.DoctorUnavailableException;
import org.example.exception.PatientAppointmentConflictException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.AppointmentRepository;
import org.example.repository.DoctorAvailabilityRepository;
import org.example.repository.DoctorRepository;
import org.example.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {


    AppointmentRepository appointmentRepository;
    DoctorRepository doctorRepository;
    DoctorAvailabilityRepository doctorAvailabilityRepository;
    PatientRepository patientRepository;


    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, DoctorAvailabilityRepository doctorAvailabilityRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
        this.patientRepository = patientRepository;
    }



    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO appointmentRequestDTO){

        Long doctorId = appointmentRequestDTO.getDoctorId();
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(()->new ResourceNotFoundException("No doctor found for id: "+doctorId));

        Long patientId = appointmentRequestDTO.getPatientId();
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(()->new ResourceNotFoundException("No patient found for id: "+patientId));


        // Doctor must be active
        if(doctor.getIsActive().equals(false)){
            throw new DoctorInactiveException("Doctor with id: "+doctorId+" is inactive");
        }


        Appointment newAppointment = new Appointment();
        newAppointment.setDoctor(doctor);
        newAppointment.setPatient(patient);
        newAppointment.setStartTime(appointmentRequestDTO.getStartTime());
        newAppointment.setEndTime(appointmentRequestDTO.getEndTime());
        newAppointment.setReasonForVisit(appointmentRequestDTO.getReasonForVisit());


        LocalDateTime newAppointmentStartTime = newAppointment.getStartTime();
        LocalDateTime newAppointmentEndTime = newAppointment.getEndTime();


        // Patient must not have two appointments at the same time

        List<Appointment> patientAppointmentList = appointmentRepository.findAllByPatientId(doctorId);

        int appointmentsCounter = 0;

        for(Appointment patientAppointment:patientAppointmentList){

            LocalDateTime paStartTime = patientAppointment.getStartTime();
            LocalDateTime paEndTime = patientAppointment.getEndTime();

            boolean isStartTimeInsideInterval = !newAppointmentStartTime.isBefore(paStartTime) && !newAppointmentStartTime.isAfter(paEndTime);
            boolean isEndTimeInsideInterval = !newAppointmentEndTime.isBefore(paStartTime) && !newAppointmentEndTime.isAfter(paEndTime);

            if(isStartTimeInsideInterval || isEndTimeInsideInterval){

                appointmentsCounter++;
            }
        }

        if(appointmentsCounter==patientAppointmentList.size()){
            throw new PatientAppointmentConflictException("Provided period conflicts with an existing appointment for patient with id: "+patientId);

        }



        //Doctor must be available

        List<DoctorAvailability> doctorAvailabilityList = doctorAvailabilityRepository.findAllByDoctorId(doctorId);
        int doctorAvailabilitiesCounter = 0;

        for(DoctorAvailability dav:doctorAvailabilityList){

            LocalDateTime davStartTime = dav.getStartTime();
            LocalDateTime davEndTime = dav.getEndTime();

            if(!newAppointmentStartTime.isBefore(davStartTime) && !newAppointmentEndTime.isAfter(davEndTime)){
                doctorAvailabilityMinusAppointment(dav,newAppointment);
                break;
            }else{
                doctorAvailabilitiesCounter++;
            }
        }

        if(doctorAvailabilitiesCounter==doctorAvailabilityList.size()){
            throw new DoctorUnavailableException("Doctor with id "+doctorId+" has no corresponding availability");
        }



        return mapToAppointmentResponseDTO(newAppointment);
    }








    @Transactional
    public void doctorAvailabilityMinusAppointment( DoctorAvailability doctorAvailability, Appointment appointment){

        DoctorAvailability newDoctorAvailabilityLeft = new DoctorAvailability(null,
                doctorAvailability.getDoctor(),
                doctorAvailability.getStartTime(),
                appointment.getStartTime());

        DoctorAvailability newDoctorAvailabilityRight = new DoctorAvailability(null,
                doctorAvailability.getDoctor(),
                appointment.getEndTime(),
                doctorAvailability.getEndTime());


        doctorAvailabilityRepository.delete(doctorAvailability);

        if(!newDoctorAvailabilityLeft.getStartTime().isEqual(newDoctorAvailabilityLeft.getEndTime())){
            doctorAvailabilityRepository.save(newDoctorAvailabilityLeft);
        }

        if(!newDoctorAvailabilityRight.getStartTime().isEqual(newDoctorAvailabilityRight.getEndTime())){
            doctorAvailabilityRepository.save(newDoctorAvailabilityRight);
        }

        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);


    }




    private AppointmentResponseDTO mapToAppointmentResponseDTO(Appointment appointment){

        AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO();

        appointmentResponseDTO.setId(appointment.getId());
        appointmentResponseDTO.setDoctorId(appointment.getDoctor().getId());
        appointmentResponseDTO.setPatientId(appointment.getPatient().getId());
        appointmentResponseDTO.setStartTime(appointment.getStartTime());
        appointmentResponseDTO.setEndTime(appointment.getEndTime());
        appointmentResponseDTO.setReasonForVisit(appointment.getReasonForVisit());
        appointmentResponseDTO.setAppointmentStatus(appointment.getAppointmentStatus());

        return appointmentResponseDTO;

    }




}
