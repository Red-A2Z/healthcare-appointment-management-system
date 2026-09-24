package org.example.service;


import jakarta.transaction.Transactional;
import org.example.dto.AppointmentDTOs.*;
import org.example.entity.Appointment;
import org.example.entity.Doctor;
import org.example.entity.DoctorAvailability;
import org.example.entity.Patient;
import org.example.enums.AppointmentStatus;
import org.example.exception.*;
import org.example.repository.AppointmentRepository;
import org.example.repository.DoctorAvailabilityRepository;
import org.example.repository.DoctorRepository;
import org.example.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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


        // Patient cannot have overlapping appointments
        List<Appointment> patientAppointmentList = appointmentRepository.findAllByPatientId(patientId);
        checkAppointmentAgainstPatientAppointments(patientAppointmentList,newAppointment);

        //Doctor must be available
        List<DoctorAvailability> doctorAvailabilityList = doctorAvailabilityRepository.findAllByDoctorId(doctorId);
        DoctorAvailability correspondingDoctorAvailability = checkAppointmentAgainstDAs(doctorAvailabilityList,newAppointment);

        saveAppointmentAndPerformRelatedActions(correspondingDoctorAvailability, newAppointment);

        return mapToAppointmentResponseDTO(newAppointment);
    }




    public void checkAppointmentAgainstPatientAppointments(List<Appointment> patientAppointmentList,Appointment newAppointment){

        Long patientId = newAppointment.getPatient().getId();
        LocalDateTime newAppointmentStartTime = newAppointment.getStartTime();
        LocalDateTime newAppointmentEndTime = newAppointment.getEndTime();


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

        if(!patientAppointmentList.isEmpty() && appointmentsCounter==patientAppointmentList.size()){
            throw new PatientAppointmentConflictException("Provided period conflicts with an existing appointment for patient with id: "+patientId);

        }

    }



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



    @Transactional
    public void saveAppointmentAndPerformRelatedActions( DoctorAvailability doctorAvailability, Appointment appointment){

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









    public AppointmentResponseDTO updateDoctorIdForAppointment(Long id,AppointmentDoctorIdPatchDTO appointmentDoctorIdPatchDTO){

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No appointment found for id: "+id));


        Long doctorId = appointmentDoctorIdPatchDTO.getDoctorId();
        if(Objects.equals(doctorId, appointment.getDoctor().getId())){
            return mapToAppointmentResponseDTO(appointment);
        }

        if(appointment.getAppointmentStatus().equals(AppointmentStatus.COMPLETED)){
            throw new AppointmentCOMPLETEDException("Appointments marked as COMPLETED cannot have doctor changed");
        }

        Doctor newDoctor = doctorRepository.findById(doctorId).orElseThrow(()-> new ResourceNotFoundException("No doctor found for id: "+doctorId));

        //Doctor must be available
        List<DoctorAvailability> doctorAvailabilityList = doctorAvailabilityRepository.findAllByDoctorId(doctorId);
        DoctorAvailability correspondingDoctorAvailability = checkAppointmentAgainstDAs(doctorAvailabilityList, appointment);

        changeDoctorAndPerformRelatedActions(newDoctor, correspondingDoctorAvailability, appointment);

        return mapToAppointmentResponseDTO(appointment);
    }


    @Transactional
    public void changeDoctorAndPerformRelatedActions(Doctor newDoctor, DoctorAvailability doctorAvailability, Appointment appointment){

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


        // Free time for old Doctor
        DoctorAvailability newAvailabilityForOldDoctor = new DoctorAvailability(null,appointment.getDoctor(),appointment.getStartTime(),appointment.getEndTime());
        doctorAvailabilityRepository.save(newAvailabilityForOldDoctor);


        appointment.setDoctor(newDoctor);
        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);



    }






    public AppointmentResponseDTO updatePatientIdForAppointment(Long id, AppointmentPatientIdPatchDTO appointmentPatientIdPatchDTO){

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No appointment found for id: "+id));


        Long patientId = appointmentPatientIdPatchDTO.getPatientId();
        if(Objects.equals(patientId, appointment.getPatient().getId())){
            return mapToAppointmentResponseDTO(appointment);
        }

        if(appointment.getAppointmentStatus().equals(AppointmentStatus.COMPLETED)){
            throw new AppointmentCOMPLETEDException("Appointments marked as COMPLETED cannot have patient changed");
        }

        Patient newPatient = patientRepository.findById(patientId).orElseThrow(()-> new ResourceNotFoundException("No patient found for id: "+patientId));

        // Patient cannot have overlapping appointments
        List<Appointment> patientAppointmentList = appointmentRepository.findAllByPatientId(patientId);
        checkAppointmentAgainstPatientAppointments(patientAppointmentList,appointment);

        appointment.setPatient(newPatient);
        appointmentRepository.save(appointment);

        return mapToAppointmentResponseDTO(appointment);
    }







    public AppointmentResponseDTO rescheduleAppointment(Long id, AppointmentReschedulingDTO appointmentReschedulingDTO){

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No appointment found for id: "+id));


        if(appointment.getAppointmentStatus().equals(AppointmentStatus.COMPLETED)){
            throw new AppointmentCOMPLETEDException("Appointments marked as COMPLETED cannot be rescheduled");
        }

        Long doctorId = appointment.getDoctor().getId();

        // Doctor must be active
        if(appointment.getDoctor().getIsActive().equals(false)){
            throw new DoctorInactiveException("Doctor with id: "+doctorId+" is inactive");
        }

        LocalDateTime startTimeToFree = appointment.getStartTime();
        LocalDateTime endTimeToFree = appointment.getEndTime();
        DoctorAvailability davWhenAppointmentPeriodIsFreed = new DoctorAvailability(null,appointment.getDoctor(),startTimeToFree,endTimeToFree);

        appointment.setStartTime(appointmentReschedulingDTO.getStartTime());
        appointment.setEndTime(appointmentReschedulingDTO.getEndTime());

        // Patient cannot have overlapping appointments
        Long patientId = appointment.getPatient().getId();
        List<Appointment> patientAppointmentList = appointmentRepository.findAllByPatientId(patientId);
        patientAppointmentList.removeIf(obj -> obj.getId().equals(id));
        checkAppointmentAgainstPatientAppointments(patientAppointmentList,appointment);

        checkAppointmentAgainstDAsAndPerformReschedulingActions(davWhenAppointmentPeriodIsFreed, appointment);


        return mapToAppointmentResponseDTO(appointment);
    }



    @Transactional
    public void checkAppointmentAgainstDAsAndPerformReschedulingActions(DoctorAvailability davWhenAppointmentPeriodIsFreed, Appointment appointment){

        doctorAvailabilityRepository.save(davWhenAppointmentPeriodIsFreed);

        //Doctor must be available
        List<DoctorAvailability> doctorAvailabilityList = doctorAvailabilityRepository.findAllByDoctorId(appointment.getDoctor().getId());
        DoctorAvailability correspondingDoctorAvailability = checkAppointmentAgainstDAs(doctorAvailabilityList,appointment);

        saveAppointmentAndPerformRelatedActions(correspondingDoctorAvailability, appointment);

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
