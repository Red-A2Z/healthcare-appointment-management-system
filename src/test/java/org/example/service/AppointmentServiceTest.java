package org.example.service;


import org.example.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    AppointmentRepository appointmentRepository;

    @InjectMocks
    AppointmentService appointmentService;



    @Test
    void checkAppointmentAgainstPatientAppointments_WhenAppointmentsOverlap_ThrowsException(){}

    @Test
    void checkAppointmentAgainstPatientAppointments_WhenThereIsNoAppointmentsOverlapping(){}


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
