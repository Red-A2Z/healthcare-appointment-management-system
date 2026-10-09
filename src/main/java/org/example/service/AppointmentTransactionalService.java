package org.example.service;


import jakarta.transaction.Transactional;
import org.example.dto.AppointmentDTOs.AppointmentResponseDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityRequestDTO;
import org.example.entity.Appointment;
import org.example.entity.Doctor;
import org.example.entity.DoctorAvailability;
import org.example.enums.AppointmentStatus;
import org.example.exception.ConflictExcpetions.children.TimePeriodAlreadyCoveredException;
import org.example.repository.AppointmentRepository;
import org.example.repository.DoctorAvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentTransactionalService {

    private final DoctorAvailabilityService doctorAvailabilityService;
    private final AppointmentHelperService appointmentHelperService;

    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final AppointmentRepository appointmentRepository;


    public AppointmentTransactionalService(DoctorAvailabilityService doctorAvailabilityService, AppointmentHelperService appointmentHelperService, DoctorAvailabilityRepository doctorAvailabilityRepository, AppointmentRepository appointmentRepository) {
        this.doctorAvailabilityService = doctorAvailabilityService;
        this.appointmentHelperService = appointmentHelperService;
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public void saveAppointmentAndPerformRelatedActions(DoctorAvailability doctorAvailability, Appointment appointment){

        DoctorAvailabilityRequestDTO newDoctorAvailabilityLeft = new DoctorAvailabilityRequestDTO(doctorAvailability.getDoctor().getId(),
                doctorAvailability.getStartTime(),
                appointment.getStartTime());

        DoctorAvailabilityRequestDTO newDoctorAvailabilityRight = new DoctorAvailabilityRequestDTO(doctorAvailability.getDoctor().getId(),
                appointment.getEndTime(),
                doctorAvailability.getEndTime());


        doctorAvailabilityRepository.delete(doctorAvailability);

        if(!newDoctorAvailabilityLeft.getStartTime().isEqual(newDoctorAvailabilityLeft.getEndTime())){
            doctorAvailabilityService.createDoctorAvailability(newDoctorAvailabilityLeft);
        }

        if(!newDoctorAvailabilityRight.getStartTime().isEqual(newDoctorAvailabilityRight.getEndTime())){
            doctorAvailabilityService.createDoctorAvailability(newDoctorAvailabilityRight);
        }

        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);


    }





    @Transactional
    public void changeDoctorAndPerformRelatedActions(Doctor newDoctor, DoctorAvailability doctorAvailability, Appointment appointment){

        DoctorAvailabilityRequestDTO newDoctorAvailabilityLeft = new DoctorAvailabilityRequestDTO(doctorAvailability.getDoctor().getId(),
                doctorAvailability.getStartTime(),
                appointment.getStartTime());

        DoctorAvailabilityRequestDTO newDoctorAvailabilityRight = new DoctorAvailabilityRequestDTO(doctorAvailability.getDoctor().getId(),
                appointment.getEndTime(),
                doctorAvailability.getEndTime());


        doctorAvailabilityRepository.delete(doctorAvailability);

        if(!newDoctorAvailabilityLeft.getStartTime().isEqual(newDoctorAvailabilityLeft.getEndTime())){
            doctorAvailabilityService.createDoctorAvailability(newDoctorAvailabilityLeft);
        }

        if(!newDoctorAvailabilityRight.getStartTime().isEqual(newDoctorAvailabilityRight.getEndTime())){
            doctorAvailabilityService.createDoctorAvailability(newDoctorAvailabilityRight);
        }


        // Free time for old Doctor
        DoctorAvailabilityRequestDTO newAvailabilityForOldDoctor = new DoctorAvailabilityRequestDTO(appointment.getDoctor().getId(),
                appointment.getStartTime(),
                appointment.getEndTime());
        doctorAvailabilityService.createDoctorAvailability(newAvailabilityForOldDoctor);


        appointment.setDoctor(newDoctor);
        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);



    }


    @Transactional
    public void checkAppointmentAgainstDAsAndPerformReschedulingActions(DoctorAvailabilityRequestDTO davWhenAppointmentPeriodIsFreed, Appointment appointment){

        doctorAvailabilityService.createDoctorAvailability(davWhenAppointmentPeriodIsFreed);

        //Doctor must be available
        List<DoctorAvailability> doctorAvailabilityList = doctorAvailabilityRepository.findAllByDoctorId(appointment.getDoctor().getId());
        DoctorAvailability correspondingDoctorAvailability = appointmentHelperService.checkAppointmentAgainstDAs(doctorAvailabilityList,appointment);

        saveAppointmentAndPerformRelatedActions(correspondingDoctorAvailability, appointment);

    }



    @Transactional
    public AppointmentResponseDTO freeTimeslotAndUpdateAppointment(Appointment appointment){

        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        // free this timeslot for Doctor
        DoctorAvailabilityRequestDTO doctorAvailability = new DoctorAvailabilityRequestDTO(appointment.getDoctor().getId(),
                appointment.getStartTime(),
                appointment.getEndTime());
        try{
            doctorAvailabilityService.createDoctorAvailability(doctorAvailability);
        }catch (TimePeriodAlreadyCoveredException ignored){

        }


        return appointmentHelperService.mapToAppointmentResponseDTO(appointment);

    }




}
