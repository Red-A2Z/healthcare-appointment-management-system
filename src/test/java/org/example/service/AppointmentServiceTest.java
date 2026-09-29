package org.example.service;


import org.example.entity.Appointment;
import org.example.entity.Patient;
import org.example.enums.AppointmentStatus;
import org.example.exception.ConflictExcpetions.children.PatientAppointmentConflictException;
import org.example.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    AppointmentRepository appointmentRepository;

    @InjectMocks
    AppointmentService appointmentService;



    @Test
    void checkAppointmentAgainstPatientAppointments_WhenAppointmentsOverlapCase1_ThrowsException(){

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
    void checkAppointmentAgainstPatientAppointments_WhenAppointmentsOverlapCase2_ThrowsException(){

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
    void checkAppointmentAgainstPatientAppointments_WhenAppointmentsOverlapCase3_ThrowsException(){

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
    void checkAppointmentAgainstPatientAppointments_WhenThereIsNoAppointmentsOverlapping_shouldNotThrowException(){}


    @Test
    void checkAppointmentAgainstDAs_WhenThereIsNoCorrespondingDoctorAvailability_ThrowsException(){}

    @Test
    void checkAppointmentAgainstDAs_WhenThereIsACorrespondingDoctorAvailability(){}


    @Test
    void saveAppointmentAndPerformRelatedActions_WhenAppointmentFullyCoversDAV_SavesRemainingDAV(){}

    @Test
    void saveAppointmentAndPerformRelatedActions_WhenAppointmentPartiallyCoversDAV_SavesRemainingDAV(){}



    // ///////////////////////////////////////////

    @Test
    void updateDoctorIdForAppointment_WhenAppointmentCanLongerBeUpdatedThrowsException(){}

    @Test
    void changeDoctorAndPerformRelatedActions_shouldFreeTimeForOldDoctor(){}



    // ///////////////////////////////////////////


    @Test
    void updateAppointmentStatus_WhenNewValueIsSCHEDULED_AndOldOneIsDifferent_ThrowsException(){}

    @Test
    void updateAppointmentStatus_WhenNewAndOldValuesAreSCHEDULED_AndAppointmentIsInThePast_ThrowsException(){}

    @Test
    void updateAppointmentStatus_WhenNewValueIsCANCELLED_AndAppointmentIsActualOrInThePast_ThrowsException(){}

    @Test
    void updateAppointmentStatus_WhenNewValueIsNotSCHEDULEDOrCANCELLED_AndAppointmentIsInThePast_ThrowsException(){}

    @Test
    void updateAppointmentStatus_WhenNewValueIsNotSCHEDULEDOrCANCELLED_AndPreviousValueIsDifferentFromSCHEDULEDAndNewValue_ThrowsException(){}


    @Test
    void freeTimeslotAndUpdateAppointment_shouldFreeTimeslotForDoctor_AndMarkAppointmentAsCancelled(){}











}
