package com.example.demo.controller;


import com.example.demo.dto.MedicalRecordDto;
import com.example.demo.exceptionhandler.PatientNotFoundException;
import com.example.demo.service.MedicalService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicalController.class)
public class MedicalControllerTest {

    @Autowired
    private MockMvc mokmvc;
    @MockBean
    private MedicalService medicalService;

    @InjectMocks
    private MedicalController medicalController;


    @Test
    void getMedicalRecord_ShouldReturnOk() throws Exception {
        MedicalRecordDto dto = new MedicalRecordDto();
        dto.setId(1L);

        when(medicalService.getMedicalRecord(1L)).thenReturn(dto);

        mokmvc.perform(get("/medical/1"))
                .andExpect(status().isOk());


    }

    @Test
    void getMedicalRecord_ShouldReturn404_WhenNotFound() throws Exception {
        when(medicalService.getMedicalRecord(1L))
                .thenThrow(new PatientNotFoundException("Not Found patient with" + 1));

        mokmvc.perform(get("/medical/1"))
                .andExpect(status().isNotFound());
    }

}

