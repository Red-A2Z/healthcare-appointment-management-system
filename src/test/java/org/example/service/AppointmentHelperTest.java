package org.example.service;


import org.example.entity.Appointment;
import org.example.entity.Doctor;
import org.example.entity.DoctorAvailability;
import org.example.exception.ConflictExcpetions.children.DoctorUnavailableException;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppointmentHelperTest {


    private final AppointmentHelperService appointmentHelperService = new AppointmentHelperService();


    @Test
    void checkAppointmentAgainstDAs_whenThereIsNoCorrespondingDoctorAvailability_throwsException(){

        Doctor doctor =  new Doctor(1L,"John","John","Cardiology","+1234567","john@john.com",true);

        DoctorAvailability doctorAvailability_1 = new DoctorAvailability(
                1L,
                doctor,
                LocalDateTime.parse("2030-09-01T08:00:00"),
                LocalDateTime.parse("2030-09-01T10:00:00"));

        List<DoctorAvailability> doctorAvailabilityList =  new ArrayList<>();
        doctorAvailabilityList.add(doctorAvailability_1);

        Appointment newAppointment = new Appointment(null,
                doctor,
                null, // we don't need a real Patient value here
                LocalDateTime.parse("2030-09-01T10:00:00"),
                LocalDateTime.parse("2030-09-01T11:00:00"),
                "Reason 1",
                null);


        DoctorUnavailableException doctorUnavailableException = assertThrows(DoctorUnavailableException.class,
                ()-> appointmentHelperService.checkAppointmentAgainstDAs(doctorAvailabilityList,newAppointment));

        assertEquals("Doctor with id "+1L+" has no corresponding availability",
                doctorUnavailableException.getMessage());

    }



    @Test
    void checkAppointmentAgainstDAs_whenThereIsACorrespondingDoctorAvailability_returnsThatAvailability(){

        Doctor doctor =  new Doctor(1L,"John","John","Cardiology","+1234567","john@john.com",true);

        DoctorAvailability doctorAvailability_1 = new DoctorAvailability(
                1L,
                doctor,
                LocalDateTime.parse("2030-09-01T08:00:00"),
                LocalDateTime.parse("2030-09-01T10:00:00"));

        DoctorAvailability doctorAvailability_2 = new DoctorAvailability(
                2L,
                doctor,
                LocalDateTime.parse("2030-09-01T10:00:00"),
                LocalDateTime.parse("2030-09-01T12:00:00"));

        DoctorAvailability doctorAvailability_3 = new DoctorAvailability(
                3L,
                doctor,
                LocalDateTime.parse("2030-09-01T12:00:00"),
                LocalDateTime.parse("2030-09-01T14:00:00"));


        List<DoctorAvailability> doctorAvailabilityList =  new ArrayList<>();
        doctorAvailabilityList.add(doctorAvailability_1);
        doctorAvailabilityList.add(doctorAvailability_2);
        doctorAvailabilityList.add(doctorAvailability_3);

        Appointment newAppointment = new Appointment(null,
                doctor,
                null, // we don't need a real Patient value here
                LocalDateTime.parse("2030-09-01T10:00:00"),
                LocalDateTime.parse("2030-09-01T11:00:00"),
                "Reason 1",
                null);

        DoctorAvailability correspondingDAV = appointmentHelperService.checkAppointmentAgainstDAs(doctorAvailabilityList,newAppointment);

        assertEquals(doctorAvailability_2,correspondingDAV);

    }



}
