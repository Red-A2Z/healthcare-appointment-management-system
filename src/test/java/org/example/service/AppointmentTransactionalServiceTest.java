package org.example.service;


import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityRequestDTO;
import org.example.entity.Appointment;
import org.example.entity.Doctor;
import org.example.entity.DoctorAvailability;
import org.example.entity.Patient;
import org.example.enums.AppointmentStatus;
import org.example.repository.AppointmentRepository;
import org.example.repository.DoctorAvailabilityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AppointmentTransactionalServiceTest {



    @Mock
    DoctorAvailabilityService doctorAvailabilityService;
    @Mock
    AppointmentHelperService appointmentHelperService;
    @Mock
    DoctorAvailabilityRepository doctorAvailabilityRepository;
    @Mock
    AppointmentRepository appointmentRepository;

    @InjectMocks
    AppointmentTransactionalService appointmentTransactionalService;




    @Test
    void saveAppointmentAndPerformRelatedActions_whenAppointmentFullyCoversDAV_savesTheAppointmentOnly(){

        Doctor doctor =  new Doctor(1L,"John","John","Cardiology","+1234567","john@john.com",true);


        DoctorAvailability doctorAvailability = new DoctorAvailability(
                1L,
                doctor,
                LocalDateTime.parse("2030-09-01T08:00:00"),
                LocalDateTime.parse("2030-09-01T12:00:00"));


        Appointment newAppointment = new Appointment(null,
                doctor,
                null, // we don't need a real Patient value here
                LocalDateTime.parse("2030-09-01T08:00:00"),
                LocalDateTime.parse("2030-09-01T12:00:00"),
                "Reason 1",
                null);


        appointmentTransactionalService.saveAppointmentAndPerformRelatedActions(doctorAvailability,newAppointment);

        verify(doctorAvailabilityRepository).delete(doctorAvailability);
        verify(doctorAvailabilityRepository,never()).save(any(DoctorAvailability.class));
        verify(appointmentRepository).save(newAppointment);


    }

    @Test
    void saveAppointmentAndPerformRelatedActions_whenAppointmentPartiallyCoversDAV_savesTheAppointmentAndRemainingDAV(){

        Doctor doctor =  new Doctor(1L,"John","John","Cardiology","+1234567","john@john.com",true);


        DoctorAvailability doctorAvailability = new DoctorAvailability(
                1L,
                doctor,
                LocalDateTime.parse("2030-09-01T08:00:00"),
                LocalDateTime.parse("2030-09-01T12:00:00"));

        Appointment newAppointment = new Appointment(null,
                doctor,
                null, // we don't need a real Patient value here
                LocalDateTime.parse("2030-09-01T09:00:00"),
                LocalDateTime.parse("2030-09-01T11:00:00"),
                "Reason 1",
                null);


        appointmentTransactionalService.saveAppointmentAndPerformRelatedActions(doctorAvailability,newAppointment);


        verify(doctorAvailabilityRepository).delete(doctorAvailability);
        verify(doctorAvailabilityService,times(2)).createDoctorAvailability(any(DoctorAvailabilityRequestDTO.class));
        verify(appointmentRepository).save(newAppointment);


    }



    @Test
    void changeDoctorAndPerformRelatedActions_shouldFreeTimeForOldDoctor(){

        Doctor oldDoctor =  new Doctor(1L,
                "John",
                "John",
                "Cardiology",
                "+1234567",
                "john@john.com",
                true);


        Doctor newDoctor =  new Doctor(2L,
                "Sam",
                "Sam",
                "Cardiology",
                "+1234567",
                "sam@sam.com",
                false);

        DoctorAvailability doctorAvailability = new DoctorAvailability(
                1L,
                newDoctor,
                LocalDateTime.parse("2030-09-01T08:00:00"),
                LocalDateTime.parse("2030-09-01T12:00:00"));


        Appointment appointment = new Appointment(1L,
                oldDoctor,
                null, // we don't need a real Patient value here
                LocalDateTime.parse("2030-09-01T09:00:00"),
                LocalDateTime.parse("2030-09-01T11:00:00"),
                "Reason 1",
                null);



        appointmentTransactionalService.changeDoctorAndPerformRelatedActions(newDoctor,doctorAvailability,appointment);


        verify(doctorAvailabilityRepository).delete(doctorAvailability);
        verify(doctorAvailabilityService,times(3)).createDoctorAvailability(any(DoctorAvailabilityRequestDTO.class));
        verify(appointmentRepository).save(appointment);

    }




    @Test
    void freeTimeslotAndUpdateAppointment_shouldFreeTimeslotForDoctor_andMarkAppointmentAsCancelled(){

        Doctor doctor =  new Doctor(1L,
                "John",
                "John",
                "Cardiology",
                "+1234567",
                "john@john.com",
                true);

        Patient patient =  new Patient(1L,
                "Hugo",
                "Hugo",
                LocalDate.of(2000,1,1),
                "+7654321",
                "hugo@hugo.com");

        Appointment appointment = new Appointment(1L,
                doctor,
                patient,
                LocalDateTime.parse("2030-09-01T09:00:00"),
                LocalDateTime.parse("2030-09-01T11:00:00"),
                "Reason 1",
                AppointmentStatus.SCHEDULED);


        appointmentTransactionalService.freeTimeslotAndUpdateAppointment(appointment);


        assertEquals(AppointmentStatus.CANCELLED,appointment.getAppointmentStatus());
        verify(appointmentRepository).save(appointment);
        verify(doctorAvailabilityService).createDoctorAvailability(any(DoctorAvailabilityRequestDTO.class));




    }



}
