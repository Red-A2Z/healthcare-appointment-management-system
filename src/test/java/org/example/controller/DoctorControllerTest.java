package org.example.controller;


import org.example.dto.DoctorDTOs.DoctorRequestDTO;
import org.example.exception.GlobalExceptionHandler;
import org.example.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(GlobalExceptionHandler.class)
public class DoctorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    DoctorService doctorService;



    @Test
    void createDoctor_whenEmailIsNotValid_throwsException() throws Exception {


        DoctorRequestDTO doctorRequestDTO =  new DoctorRequestDTO("John","John","Cardiology","+1234567","john.com",true);


        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("email","Email should be valid");


        mockMvc.perform(post("/api/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageMap").value(messageMap));


    }



    @Test
    void updateDoctor_whenPhoneNumberIsNotValid_throwsException() throws Exception {

        DoctorRequestDTO doctorRequestDTO =  new DoctorRequestDTO("John","John","Cardiology","+123","john@john.com",true);

        Map<String,String> map = new HashMap<>();
        map.put("phoneNumber","Phone number should be valid");

        mockMvc.perform(put("/api/doctor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageMap").value(map));



    }

    @Test
    void updateSomeFieldsForDoctor_whenEmailIsNotValid_throwsException() throws Exception {


        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("email","john.com");

        String requestBody = objectMapper.writeValueAsString(requestBodyMap);


        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("email","Email should be valid");


        mockMvc.perform(patch("/api/doctor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageMap").value(messageMap));


    }





}


