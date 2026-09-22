package org.example.service;


import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityPatchRequestDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityRequestDTO;
import org.example.entity.Doctor;
import org.example.entity.DoctorAvailability;
import org.example.exception.InvalidDateRangeException;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.TimePeriodAlreadyCoveredException;
import org.example.repository.DoctorAvailabilityRepository;
import org.example.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorAvailabilityServiceTest {

    @Mock
    DoctorRepository doctorRepository;

    @Mock
    DoctorAvailabilityRepository doctorAvailabilityRepository;

    @InjectMocks
    DoctorAvailabilityService doctorAvailabilityService;


    @Test
    void createDoctorAvailability_whenNoDoctorFoundForIdExists_throwsException(){

        DoctorAvailabilityRequestDTO doctorAvailabilityRequestDTO = new DoctorAvailabilityRequestDTO(1L,
                LocalDateTime.parse("2030-09-19T10:00:00"),
                LocalDateTime.parse("2030-09-20T16:30:00"));


        when(doctorRepository.findById(doctorAvailabilityRequestDTO.getDoctorId())).thenReturn(Optional.empty());


        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                ()-> doctorAvailabilityService.createDoctorAvailability(doctorAvailabilityRequestDTO));

        assertEquals("No doctor found for id: " + doctorAvailabilityRequestDTO.getDoctorId(),resourceNotFoundException.getMessage());

        verify(doctorAvailabilityRepository, never()).save(any(DoctorAvailability.class));

    }



    @Test
     void createDoctorAvailability_whenProvidedPeriodIsAlreadyCovered_ThrowsException(){

        DoctorAvailabilityRequestDTO doctorAvailabilityRequestDTO = new DoctorAvailabilityRequestDTO(1L,
                LocalDateTime.parse("2030-09-01T12:00:00"),
                LocalDateTime.parse("2030-09-01T14:00:00"));



        Long doctorId = doctorAvailabilityRequestDTO.getDoctorId();


        Doctor doctor = new Doctor(1L,"John","John","Cardiology","+1234567","john@john.com",true);


        List<DoctorAvailability> doctorAvailabilitiesList = new ArrayList<>();
        DoctorAvailability existingDoctorAvailability = new DoctorAvailability(1L,
                doctor,
                LocalDateTime.parse("2030-09-01T10:00:00"),
                LocalDateTime.parse("2030-09-01T16:30:00"));
        doctorAvailabilitiesList.add(existingDoctorAvailability);



        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(doctorAvailabilityRepository.findAllByDoctorId(doctorId)).thenReturn(doctorAvailabilitiesList);


        TimePeriodAlreadyCoveredException timePeriodAlreadyCoveredException = assertThrows(TimePeriodAlreadyCoveredException.class,
                ()-> doctorAvailabilityService.createDoctorAvailability(doctorAvailabilityRequestDTO));


        assertEquals("The provided period is already covered by an existing one",timePeriodAlreadyCoveredException.getMessage());

        verify(doctorAvailabilityRepository,never()).delete(any(DoctorAvailability.class));
        verify(doctorAvailabilityRepository,never()).save(any(DoctorAvailability.class));


    }



    @Test
    void handlePeriodOverlapBeforeSaving_whenProvidedPeriodOverlapsWIthExistingOnes_handlesIt(){


        Doctor doctor = new Doctor(1L,"John","John","Cardiology","+1234567","john@john.com",true);

        List<DoctorAvailability> doctorAvailabilitiesList = getDoctorAvailabilitiesList(doctor);

        DoctorAvailability newdoctorAvailability = new DoctorAvailability(1L,
                doctor,
                LocalDateTime.parse("2030-09-01T09:00:00"),
                LocalDateTime.parse("2030-09-01T16:00:00"));


        List<DoctorAvailability> expectedDoctorAvailabilitiesToDelete = new ArrayList<>();
        expectedDoctorAvailabilitiesToDelete.add(doctorAvailabilitiesList.get(2));
        expectedDoctorAvailabilitiesToDelete.add(doctorAvailabilitiesList.get(3));
        expectedDoctorAvailabilitiesToDelete.add(doctorAvailabilitiesList.get(1));
        expectedDoctorAvailabilitiesToDelete.add(doctorAvailabilitiesList.get(0));


        List<DoctorAvailability> actualDoctorAvailabilitiesToDelete = doctorAvailabilityService.handlePeriodOverlapBeforeSaving(doctorAvailabilitiesList,newdoctorAvailability);


        assertEquals(expectedDoctorAvailabilitiesToDelete, actualDoctorAvailabilitiesToDelete);
        assertEquals(LocalDateTime.parse("2030-09-01T08:00:00"),newdoctorAvailability.getStartTime());
        assertEquals(LocalDateTime.parse("2030-09-01T17:00:00"),newdoctorAvailability.getEndTime());



    }




    private  List<DoctorAvailability> getDoctorAvailabilitiesList(Doctor doctor){

        DoctorAvailability existingDoctorAvailability_1 = new DoctorAvailability(1L,
                doctor,
                LocalDateTime.parse("2030-09-01T15:00:00"),
                LocalDateTime.parse("2030-09-01T17:00:00"));

        DoctorAvailability existingDoctorAvailability_2 = new DoctorAvailability(2L,
                doctor,
                LocalDateTime.parse("2030-09-01T08:00:00"),
                LocalDateTime.parse("2030-09-01T10:00:00"));

        DoctorAvailability existingDoctorAvailability_3 = new DoctorAvailability(3L,
                doctor,
                LocalDateTime.parse("2030-09-01T11:00:00"),
                LocalDateTime.parse("2030-09-01T12:00:00"));

        DoctorAvailability existingDoctorAvailability_4 = new DoctorAvailability(4L,
                doctor,
                LocalDateTime.parse("2030-09-01T13:00:00"),
                LocalDateTime.parse("2030-09-01T14:00:00"));


        List<DoctorAvailability> doctorAvailabilitiesList = new ArrayList<>();
        doctorAvailabilitiesList.add(existingDoctorAvailability_1);
        doctorAvailabilitiesList.add(existingDoctorAvailability_2);
        doctorAvailabilitiesList.add(existingDoctorAvailability_3);
        doctorAvailabilitiesList.add(existingDoctorAvailability_4);


        return doctorAvailabilitiesList;

    }








    @Test
    void updateSomeFieldsForDoctorAvailability_whenNewStartTimeIsAfterOrEqualsExistingEndTime_throwsException(){

        Long doctorAvailabilityId  = 1L;

        DoctorAvailabilityPatchRequestDTO doctorAvailabilityPatchRequestDTO = new DoctorAvailabilityPatchRequestDTO(
                JsonNullable.undefined(),
                JsonNullable.of(LocalDateTime.parse("2030-09-30T10:00:00")),
                JsonNullable.undefined()
        );

        Doctor doctor = new Doctor(1L,"John","John","Cardiology","+1234567","john@john.com",true);


        DoctorAvailability doctorAvailability = new DoctorAvailability(1L,
                doctor,
                LocalDateTime.parse("2030-09-19T10:00:00"),
                LocalDateTime.parse("2030-09-20T16:30:00"));

        when(doctorAvailabilityRepository.findById(doctorAvailabilityId)).thenReturn(Optional.of(doctorAvailability));




        InvalidDateRangeException invalidDateRangeException = assertThrows(
                InvalidDateRangeException.class,
                ()->doctorAvailabilityService.updateSomeFieldsForDoctorAvailability(
                        doctorAvailabilityId,
                        doctorAvailabilityPatchRequestDTO));

        assertEquals("Start time shouldn't be after end time. Consider checking the already saved values",invalidDateRangeException.getMessage());

        verify(doctorAvailabilityRepository,never()).save(any(DoctorAvailability.class));


















    }

}
