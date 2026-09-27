package org.example.service;

import org.example.dto.DoctorDTOs.DoctorRequestDTO;
import org.example.exception.ConflictExcpetions.children.DuplicateResourceException;
import org.example.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @Mock
    DoctorRepository doctorRepository;

    @InjectMocks
    DoctorService doctorService;


    @Test
    void createDoctor_whenEmailAlreadyExists_throwsException(){

        DoctorRequestDTO doctorRequestDTO = new DoctorRequestDTO("John","John","Cardiology","+1234567","john@john.com",true);

        when(doctorRepository.existsByEmail(doctorRequestDTO.getEmail())).thenReturn(true);

        DuplicateResourceException duplicateResourceException =  assertThrows(DuplicateResourceException.class, ()->doctorService.createDoctor(doctorRequestDTO));

        assertEquals("Email john@john.com already exists", duplicateResourceException.getMessage());


    }














}
