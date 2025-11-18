package com.example.demo.controller;

import com.example.demo.dto.PrescriptionDto;
import com.example.demo.entity.Prescription;
import com.example.demo.service.PrescriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrescriptionController.class)
public class PrescriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PrescriptionService prescriptionService;
    @Autowired
    private ObjectMapper objectMapper;


    private PrescriptionDto dto;

    @BeforeEach
    void setup() {

        dto = new PrescriptionDto();
        dto.setAppointmentId(1L);
        dto.setDoctorId(1L);
        dto.setPatientId(1L);
        dto.setDiagnosis("kook");
        dto.setMedications("vitamine");
        dto.setStatus(Prescription.Status.ACTIVE);


    }

    @Test
    void test_createPrescription() throws Exception {

        when(prescriptionService.createPrescription(any(PrescriptionDto.class))).thenReturn(dto);
        mockMvc.perform(post("/prescription/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void test_updatePrescription() throws Exception {

        when(prescriptionService.updatePrescription(eq(1L),any(PrescriptionDto.class))).thenReturn(dto);
        mockMvc.perform(put("/prescription/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }


    @Test
    void test_getPrescriptionId() throws Exception {

        when(prescriptionService.getPrescriptionId(10L)).thenReturn(dto);

         mockMvc.perform(get("/prescription/10"))
                 .andExpect(jsonPath("$.diagnosis").value("kook"))

                     .andExpect(status().isOk());


    }

    @Test
    void test_getAllprescriptions() throws Exception {
        when(prescriptionService.getAllprescriptions()).thenReturn(List.of(dto));

        mockMvc.perform(get("/prescription"))
                .andExpect(status().isOk());


    }

    @Test
    void test_getAllPrescriptionforPatientIdAndStatus() throws Exception {

        when(prescriptionService.getAllPrescriptionforPatientIdAndStatus(1L, Prescription.Status.ACTIVE)).thenReturn(List.of(dto));

        mockMvc.perform(get("/prescription/allprescriptionforpatient/1/ACTIVE"))
                .andExpect(status().isOk());


    }


    @Test
    void test_getAllPrescriptionforDoctorIdAndStatus() throws Exception {

        when(prescriptionService.getAllPrescriptionforDoctorIdAndStatus(1L, Prescription.Status.ACTIVE)).thenReturn(List.of(dto));

        mockMvc.perform(get("/prescription/allprescriptionfordoctor/1/ACTIVE"))
                .andExpect(status().isOk());


    }

    @Test
    void test_delete() throws Exception {

        doNothing().when(prescriptionService).deletePrescription(1L, Prescription.Status.DELETED);

        mockMvc.perform(delete("/prescription/1/DELETED"))
                .andExpect(status().isOk());
    }



}
