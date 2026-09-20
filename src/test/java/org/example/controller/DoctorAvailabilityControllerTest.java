package org.example.controller;


import org.example.dto.DoctorAvailabilityDTOs.DoctorAvailabilityRequestDTO;
import org.example.exception.GlobalExceptionHandler;
import org.example.service.DoctorAvailabilityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorAvailabilityController.class)
@Import(GlobalExceptionHandler.class)
public class DoctorAvailabilityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    DoctorAvailabilityService doctorAvailabilityService;



    @Test
    void createDoctorAvailability_whenStartTimeIsInThePast_throwsException() throws Exception {

        DoctorAvailabilityRequestDTO doctorAvailabilityRequestDTO = new DoctorAvailabilityRequestDTO(1L,
                LocalDateTime.parse("2026-09-19T10:00:00"),
                LocalDateTime.parse("2030-09-20T16:30:00"));


        Map<String,String> responseMap = new HashMap<>();
        responseMap.put("startTime","Start time shouldn't be in the past");

        mockMvc.perform(post("/api/doctoravailability")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(doctorAvailabilityRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageMap").value(responseMap));



    }




}
