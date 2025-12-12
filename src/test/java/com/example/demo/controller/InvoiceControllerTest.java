package com.example.demo.controller;


import com.example.demo.dto.InvoiceDto;
import com.example.demo.dto.InvoiceResponseDto;
import com.example.demo.entity.Invoice;
import com.example.demo.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    @Autowired
    private ObjectMapper objectMapper;


    private InvoiceDto invoiceDto;
    private InvoiceResponseDto responseDto;


    @BeforeEach
    void setup() {


        invoiceDto = new InvoiceDto();
        invoiceDto.setId(1L);
        invoiceDto.setDoctorId(1L);
        invoiceDto.setDoctorName("Dr.Ahmed");
        invoiceDto.setPatientId(1L);
        invoiceDto.setPatientName("Shams");
        invoiceDto.setAppointmentId(1L);
        invoiceDto.setStatus(Invoice.Status.PENDING);

        responseDto = new InvoiceResponseDto();
        responseDto.setId(1L);
        responseDto.setDoctorId(1L);
        responseDto.setDoctorName("Dr.Ahmed");
        responseDto.setPatientId(1L);
        responseDto.setPatientName("Shams");
        responseDto.setAppointmentId(1L);
        responseDto.setStatus(Invoice.Status.PENDING);


    }

    @Test
    void test_createInvoice() throws Exception {


        when(invoiceService.createInvoice(any(InvoiceDto.class))).thenReturn(responseDto);
        mockMvc.perform(post("/invoice/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invoiceDto)))
                .andExpect(status().isCreated());
    }


    @Test
    void test_getInvoicesForPatient() throws Exception {


        when(invoiceService.getInvoicesForPatient(1L)).thenReturn(List.of(responseDto));
        mockMvc.perform(get("/invoice/patientid/1"))
                .andExpect(status().isOk());


    }

    @Test
    void test_removeInvoice() throws Exception {


        Mockito.doNothing().when(invoiceService).removeInvoice(1L);
        mockMvc.perform(delete("/invoice/1"))
                .andExpect(status().isOk());

    }

    @Test
    void test_update_Invoice() throws Exception {


        when(invoiceService.updateInvoice(eq(1L), any(InvoiceDto.class))).thenReturn(responseDto);
        mockMvc.perform(put("/invoice/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invoiceDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorName").value("Dr.Ahmed"));


    }


    @Test
    void test_getInvoice() throws Exception {


        when(invoiceService.getInvoice()).thenReturn((List.of(responseDto)));
        mockMvc.perform(get("/invoice"))
                .andExpect(status().isOk());

    }

    @Test
    void test_cancelInvoice() throws Exception {


        when(invoiceService.cancelInvoice(1L)).thenReturn(responseDto);
        mockMvc.perform(get("/invoice/1"))
                .andExpect(status().isOk());

    }

    @Test
    void test_getInvoiceId() throws Exception {

        when(invoiceService.getInvoiceById(1L)).thenReturn(responseDto);
        mockMvc.perform(get("/invoice/1"))
                .andExpect(status().isOk());

    }

}
