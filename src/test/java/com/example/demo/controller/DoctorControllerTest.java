package com.example.demo.controller;

import com.example.demo.dto.DoctorDto;
import com.example.demo.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DoctorControllerTest {


    private MockMvc mockMvc;
    @Mock
    private DoctorService doctorService;
    @InjectMocks
    private DoctorController doctorController;

    private DoctorDto dto;

    @BeforeEach
    void setup() {


        dto = new DoctorDto();
        dto.setEmail("ahmed@gmail.com");
        dto.setFullName("ahmed magdy");
        dto.setSpecialization("cardio");
        dto.setPhoneNumber("0048454548");
        mockMvc = MockMvcBuilders.standaloneSetup(doctorController).build();

    }


    @Test
    void test_create_Doctor() throws Exception {

        when(doctorService.createdoctor(any(DoctorDto.class))).thenReturn(dto);

        mockMvc.perform(post("/doctor/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.email").value("ahmed@gmail.com"));



    }


    @Test
    void test_update_doctor() throws Exception{

        when(doctorService.update(eq(1L),any(DoctorDto.class))).thenReturn(dto);
        mockMvc.perform(put("/doctor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))) // convert java object to json
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ahmed@gmail.com"));

    }


    @Test
    void test_find_doctor_with_id() throws Exception{

        when(doctorService.findDoctorById(1L)).thenReturn(dto);
        mockMvc.perform(get("/doctor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ahmed@gmail.com"));

    }


    @Test
    void test_Delete_patient() throws Exception {
        mockMvc.perform(delete("/doctor/1"))
                .andExpect(status().isOk());
    }



}
