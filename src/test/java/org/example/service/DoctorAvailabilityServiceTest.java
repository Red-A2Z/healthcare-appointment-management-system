package org.example.service;


import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityPatchRequestDTO;
import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityRequestDTO;
import org.example.entity.DoctorAvailability;
import org.example.exception.InvalidDateRangeException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.DoctorAvailabilityRepository;
import org.example.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDateTime;
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
    void updateSomeFieldsForDoctorAvailability_whenNewStartTimeIsAfterOrEqualsExistingEndTime_throwsException(){


        Long doctorAvailabilityId  = 1L;

        DoctorAvailabilityPatchRequestDTO doctorAvailabilityPatchRequestDTO = new DoctorAvailabilityPatchRequestDTO(
                JsonNullable.undefined(),
                JsonNullable.of(LocalDateTime.parse("2030-09-30T10:00:00")),
                JsonNullable.undefined()
        );



        DoctorAvailability doctorAvailability = new DoctorAvailability(1L,
                null,
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
