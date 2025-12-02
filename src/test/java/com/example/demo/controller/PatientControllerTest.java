package com.example.demo.controller;


import com.example.demo.dto.PatientDto;
import com.example.demo.service.PatientService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {


    private MockMvc mockMvc;


    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;


    private PatientDto dto;

    @BeforeEach
    void setup() {

        dto = new PatientDto();
        dto.setEmail("ahmed@gmail.com");
        mockMvc= MockMvcBuilders.standaloneSetup(patientController).build();
    }


    @Test
    void test_create_patient() throws Exception{

        when(patientService.createpatient(any(PatientDto.class))).thenReturn(dto);
        mockMvc.perform(post("/patient/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))) // convert java object to json
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ahmed@gmail.com"));

    }


    @Test
    void test_update_patient() throws Exception{

        when(patientService.update(eq(1L),any(PatientDto.class))).thenReturn(dto);
        mockMvc.perform(put("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))) // convert java object to json
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ahmed@gmail.com"));

    }


    @Test
    void test_find_patient_with_id() throws Exception{

        when(patientService.findPatientById(1L)).thenReturn(dto);
        mockMvc.perform(get("/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ahmed@gmail.com"));

    }


    @Test
    void test_Delete_patient() throws Exception {
        mockMvc.perform(delete("/patient/1"))
                .andExpect(status().isOk());
    }



}
