package org.example.service;


import org.example.dto.AppointmentDTOs.AppointmentDoctorIdPatchDTO;
import org.example.dto.AppointmentDTOs.AppointmentStatusRequestDTO;
import org.example.entity.Appointment;
import org.example.entity.Doctor;
import org.example.entity.Patient;
import org.example.enums.AppointmentStatus;
import org.example.exception.ConflictExcpetions.children.AppointmentStatusConflictExceptions.AppointmentStatusTimingConflictException;
import org.example.exception.ConflictExcpetions.children.AppointmentStatusConflictExceptions.AppointmentStatusValueConflictException;
import org.example.exception.ConflictExcpetions.children.DoctorInactiveException;
import org.example.exception.ConflictExcpetions.children.PatientAppointmentConflictException;
import org.example.repository.AppointmentRepository;
import org.example.repository.DoctorAvailabilityRepository;
import org.example.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {


    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Mock
    DoctorAvailabilityService doctorAvailabilityService;

    @Mock
    DoctorRepository doctorRepository;

    @InjectMocks
    AppointmentService appointmentService;



    @Test
    void checkAppointmentAgainstPatientAppointments_whenAppointmentsOverlapCase1_throwsException(){

        Patient patient =  new Patient(1L,
                "Hugo",
                "Hugo",
                LocalDate.of(2000,1,1),
                "+7654321",
                "hugo@hugo.com");

        Appointment appointment_1 = new Appointment(1L,
                null, // we don't need a real Doctor value here
                patient,
                LocalDateTime.parse("2030-09-01T08:00:00"),
                LocalDateTime.parse("2030-09-01T10:00:00"),
                "Reason 1",
                AppointmentStatus.SCHEDULED);

        Appointment appointment_2 = new Appointment(2L,
                null, // we don't need a real Doctor value here
                patient,
                LocalDateTime.parse("2030-09-01T10:00:00"),
                LocalDateTime.parse("2030-09-01T12:00:00"),
                "Reason 1",
                AppointmentStatus.SCHEDULED);

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment_1);
        appointments.add(appointment_2);

        Appointment newAppointment = new Appointment(null,
                null, // we don't need a real Doctor value here
                patient,
                LocalDateTime.parse("2030-09-01T09:00:00"),
                LocalDateTime.parse("2030-09-01T11:00:00"),
                "Reason 3",
                null);


        PatientAppointmentConflictException patientAppointmentConflictException = assertThrows(PatientAppointmentConflictException.class,
                ()-> appointmentService.checkAppointmentAgainstPatientAppointments(appointments,newAppointment));

        assertEquals("Provided period conflicts with an existing appointment for patient with id: " + 1L,
                patientAppointmentConflictException.getMessage());


    }


    @Test
    void checkAppointmentAgainstPatientAppointments_whenAppointmentsOverlapCase2_throwsException(){

        Patient patient =  new Patient(1L,
                "Hugo",
                "Hugo",
                LocalDate.of(2000,1,1),
                "+7654321",
                "hugo@hugo.com");

        Appointment appointment_1 = new Appointment(1L,
                null, // we don't need a real Doctor value here
                patient,
                LocalDateTime.parse("2030-09-01T08:00:00"),
                LocalDateTime.parse("2030-09-01T10:00:00"),
                "Reason 1",
                AppointmentStatus.SCHEDULED);

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment_1);

        Appointment newAppointment = new Appointment(null,
                null, // we don't need a real Doctor value here
                patient,
                LocalDateTime.parse("2030-09-01T08:00:00"),
                LocalDateTime.parse("2030-09-01T11:00:00"),
                "Reason 2",
                null);


        PatientAppointmentConflictException patientAppointmentConflictException = assertThrows(PatientAppointmentConflictException.class,
                ()-> appointmentService.checkAppointmentAgainstPatientAppointments(appointments,newAppointment));

        assertEquals("Provided period conflicts with an existing appointment for patient with id: " + 1L,
                patientAppointmentConflictException.getMessage());


    }


    @Test
    void checkAppointmentAgainstPatientAppointments_whenAppointmentsOverlapCase3_throwsException(){

        Patient patient =  new Patient(1L,
                "Hugo",
                "Hugo",
                LocalDate.of(2000,1,1),
                "+7654321",
                "hugo@hugo.com");

        Appointment appointment_1 = new Appointment(1L,
                null, // we don't need a real Doctor value here
                patient,
                LocalDateTime.parse("2030-09-01T09:00:00"),
                LocalDateTime.parse("2030-09-01T10:00:00"),
                "Reason 1",
                AppointmentStatus.SCHEDULED);

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment_1);

        Appointment newAppointment = new Appointment(null,
                null, // we don't need a real Doctor value here
                patient,
                LocalDateTime.parse("2030-09-01T08:00:00"),
                LocalDateTime.parse("2030-09-01T10:00:00"),
                "Reason 2",
                null);


        PatientAppointmentConflictException patientAppointmentConflictException = assertThrows(PatientAppointmentConflictException.class,
                ()-> appointmentService.checkAppointmentAgainstPatientAppointments(appointments,newAppointment));

        assertEquals("Provided period conflicts with an existing appointment for patient with id: " + 1L,
                patientAppointmentConflictException.getMessage());


    }



    @Test
    void checkAppointmentAgainstPatientAppointments_whenThereIsNoAppointmentsOverlapping_shouldNotThrowException(){

        Patient patient =  new Patient(1L,
                "Hugo",
                "Hugo",
                LocalDate.of(2000,1,1),
                "+7654321",
                "hugo@hugo.com");

        Appointment appointment_1 = new Appointment(1L,
                null, // we don't need a real Doctor value here
                patient,
                LocalDateTime.parse("2030-09-01T08:00:00"),
                LocalDateTime.parse("2030-09-01T09:00:00"),
                "Reason 1",
                AppointmentStatus.SCHEDULED);

        Appointment appointment_2 = new Appointment(2L,
                null, // we don't need a real Doctor value here
                patient,
                LocalDateTime.parse("2030-09-01T09:00:00"),
                LocalDateTime.parse("2030-09-01T10:00:00"),
                "Reason 2",
                AppointmentStatus.SCHEDULED);

        Appointment appointment_3 = new Appointment(3L,
                null, // we don't need a real Doctor value here
                patient,
                LocalDateTime.parse("2030-09-01T11:00:00"),
                LocalDateTime.parse("2030-09-01T12:00:00"),
                "Reason 3",
                AppointmentStatus.SCHEDULED);

        Appointment appointment_4 = new Appointment(4L,
                null, // we don't need a real Doctor value here
                patient,
                LocalDateTime.parse("2030-09-01T12:00:00"),
                LocalDateTime.parse("2030-09-01T13:00:00"),
                "Reason 4",
                AppointmentStatus.SCHEDULED);

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment_1);
        appointments.add(appointment_2);
        appointments.add(appointment_3);
        appointments.add(appointment_4);


        Appointment newAppointment = new Appointment(null,
                null, // we don't need a real Doctor value here
                patient,
                LocalDateTime.parse("2030-09-01T10:00:00"),
                LocalDateTime.parse("2030-09-01T11:00:00"),
                "Reason 5",
                null);


        assertDoesNotThrow(()->appointmentService.checkAppointmentAgainstPatientAppointments(appointments,newAppointment));

    }



    // ///////////////////////////////////////////

    @Test
    void updateDoctorIdForAppointment_whenAppointmentCanLongerBeUpdatedThrowsException(){

        Long appointmentId = 1L;

        AppointmentDoctorIdPatchDTO appointmentDoctorIdPatchDTO = new AppointmentDoctorIdPatchDTO();
        appointmentDoctorIdPatchDTO.setDoctorId(2L);

        Doctor oldDoctor =  new Doctor(1L,
                "John",
                "John",
                "Cardiology",
                "+1234567",
                "john@john.com",
                true);



        Appointment appointment =  new Appointment(null,
                oldDoctor,
                null, // we don't need a real Patient value here
                LocalDateTime.parse("2030-09-01T08:00:00"),
                LocalDateTime.parse("2030-09-01T12:00:00"),
                "Reason 1",
                AppointmentStatus.SCHEDULED);

        Doctor newDoctor =  new Doctor(appointmentDoctorIdPatchDTO.getDoctorId(),
                "Sam",
                "Sam",
                "Cardiology",
                "+1234567",
                "sam@sam.com",
                false);



        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        when(doctorRepository.findById(appointmentDoctorIdPatchDTO.getDoctorId())).thenReturn(Optional.of(newDoctor));


        DoctorInactiveException doctorInactiveException = assertThrows(DoctorInactiveException.class,
                ()->appointmentService.updateDoctorIdForAppointment(appointmentId,appointmentDoctorIdPatchDTO));

        assertEquals("Doctor with id: "+appointmentDoctorIdPatchDTO.getDoctorId()+" is inactive",doctorInactiveException.getMessage());


    }



    // ///////////////////////////////////////////


    @Test
    void updateAppointmentStatus_whenNewValueIsSCHEDULED_andOldOneIsDifferent_throwsException(){

        Long appointmentId = 1L;

        AppointmentStatusRequestDTO appointmentStatusRequestDTO = new AppointmentStatusRequestDTO();
        appointmentStatusRequestDTO.setAppointmentStatus(AppointmentStatus.SCHEDULED);

        Appointment appointment = new Appointment(1L,
                null, // we don't need a real Doctor value here
                null, // we don't need a real Patient value here
                LocalDateTime.parse("2030-09-01T09:00:00"),
                LocalDateTime.parse("2030-09-01T11:00:00"),
                "Reason 1",
                AppointmentStatus.CANCELLED);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        AppointmentStatusValueConflictException appointmentStatusValueConflictException = assertThrows(
                AppointmentStatusValueConflictException.class,
                ()->appointmentService.updateAppointmentStatus(appointmentId,appointmentStatusRequestDTO));

        assertEquals("You can't directly change from other statuses to SCHEDULED",
                appointmentStatusValueConflictException.getMessage());

    }

    @Test
    void updateAppointmentStatus_whenNewAndOldValuesAreSCHEDULED_andAppointmentIsInThePast_throwsException(){

        Long appointmentId = 1L;

        AppointmentStatusRequestDTO appointmentStatusRequestDTO = new AppointmentStatusRequestDTO();
        appointmentStatusRequestDTO.setAppointmentStatus(AppointmentStatus.SCHEDULED);

        Appointment appointment = new Appointment(1L,
                null, // we don't need a real Doctor value here
                null, // we don't need a real Patient value here
                LocalDateTime.parse("2020-09-01T09:00:00"),
                LocalDateTime.parse("2020-09-01T11:00:00"),
                "Reason 1",
                AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        AppointmentStatusTimingConflictException appointmentStatusTimingConflictException = assertThrows(
                AppointmentStatusTimingConflictException.class,
                ()->appointmentService.updateAppointmentStatus(appointmentId,appointmentStatusRequestDTO));

        assertEquals("Consider updating the appointmentStatus field with a value other than SCHEDULED",
                appointmentStatusTimingConflictException.getMessage());

    }

    @Test
    void updateAppointmentStatus_whenNewValueIsCANCELLED_andAppointmentIsActualOrInThePast_throwsException(){

        Long appointmentId = 1L;

        AppointmentStatusRequestDTO appointmentStatusRequestDTO = new AppointmentStatusRequestDTO();
        appointmentStatusRequestDTO.setAppointmentStatus(AppointmentStatus.CANCELLED);

        Appointment appointment = new Appointment(1L,
                null, // we don't need a real Doctor value here
                null, // we don't need a real Patient value here
                LocalDateTime.parse("2020-09-01T09:00:00"),
                LocalDateTime.parse("2020-09-01T11:00:00"),
                "Reason 1",
                AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        AppointmentStatusTimingConflictException appointmentStatusTimingConflictException = assertThrows(
                AppointmentStatusTimingConflictException.class,
                ()->appointmentService.updateAppointmentStatus(appointmentId,appointmentStatusRequestDTO));

        assertEquals("Too late to cancel the appointment",
                appointmentStatusTimingConflictException.getMessage());

    }

    @Test
    void updateAppointmentStatus_whenNewValueIsNotSCHEDULEDOrCANCELLED_andAppointmentIsNotInThePast_throwsException(){

        Long appointmentId = 1L;

        AppointmentStatusRequestDTO appointmentStatusRequestDTO = new AppointmentStatusRequestDTO();
        appointmentStatusRequestDTO.setAppointmentStatus(AppointmentStatus.NO_SHOW);

        Appointment appointment = new Appointment(1L,
                null, // we don't need a real Doctor value here
                null, // we don't need a real Patient value here
                LocalDateTime.parse("2030-09-01T09:00:00"),
                LocalDateTime.parse("2030-09-01T11:00:00"),
                "Reason 1",
                AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        AppointmentStatusTimingConflictException appointmentStatusTimingConflictException = assertThrows(
                AppointmentStatusTimingConflictException.class,
                ()->appointmentService.updateAppointmentStatus(appointmentId,appointmentStatusRequestDTO));

        assertEquals("Too early to mark this appointment as "+appointmentStatusRequestDTO.getAppointmentStatus(),
                appointmentStatusTimingConflictException.getMessage());

    }

    @Test
    void updateAppointmentStatus_whenNewValueIsNotSCHEDULEDOrCANCELLED_andPreviousValueIsDifferentFromSCHEDULEDAndNewValue_throwsException(){

        Long appointmentId = 1L;

        AppointmentStatusRequestDTO appointmentStatusRequestDTO = new AppointmentStatusRequestDTO();
        appointmentStatusRequestDTO.setAppointmentStatus(AppointmentStatus.NO_SHOW);

        Appointment appointment = new Appointment(1L,
                null, // we don't need a real Doctor value here
                null, // we don't need a real Patient value here
                LocalDateTime.parse("2020-09-01T09:00:00"),
                LocalDateTime.parse("2020-09-01T11:00:00"),
                "Reason 1",
                AppointmentStatus.CANCELLED);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        AppointmentStatusValueConflictException appointmentStatusValueConflictException = assertThrows(
                AppointmentStatusValueConflictException.class,
                ()->appointmentService.updateAppointmentStatus(appointmentId,appointmentStatusRequestDTO));

        assertEquals("Previous value must be SCHEDULED or "+appointmentStatusRequestDTO.getAppointmentStatus()+ "in order to apply changes",
                appointmentStatusValueConflictException.getMessage());


    }













}
