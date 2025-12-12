package com.example.demo.controller;

import com.example.demo.dto.InvoiceDto;
import com.example.demo.dto.InvoiceItemDto;
import com.example.demo.entity.Invoice;
import com.example.demo.service.InvoiceItemService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvoiceItemController.class)
public class InvoiceItemControllerTest {

    @Autowired
    private MockMvc mokmvc;

    @MockBean
    private InvoiceItemService invoiceItemService;
    @Autowired
    private ObjectMapper objectMapper;

    private InvoiceItemDto invoiceItemDto;

    private InvoiceDto invoiceDto;

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

        invoiceItemDto = new InvoiceItemDto();
        invoiceItemDto.setId(1L);
        invoiceItemDto.setInvoiceId(1L);
        invoiceItemDto.setPrice(200.00);
        invoiceItemDto.setQuantity(4);
        invoiceItemDto.setServiceName("Blood Test");


    }


    @Test
    void test_update() throws Exception {


        when(invoiceItemService.updateInvoiceItems(eq(1L), eq(1L), any(InvoiceItemDto.class))).thenReturn(invoiceItemDto);
        mokmvc.perform(put("/invoiceitem/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invoiceItemDto)))
                .andExpect(status().isOk());


    }


    @Test
    void test_remove_InvoiceItem() throws Exception {


        Mockito.doNothing().when(invoiceItemService).removeInvoiceItem(1L, 1L);
        mokmvc.perform(delete("/invoiceitem/1/1"))
                .andExpect(status().isOk());

    }


    @Test
    void test_getInvoiceItem() throws Exception {


        when(invoiceItemService.getInvoiceItem()).thenReturn(List.of(invoiceItemDto));
        mokmvc.perform(get("/invoiceitem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(invoiceItemDto)))
                .andExpect(status().isOk());


    }


    @Test
    void test_getInvoiceId() throws Exception {

        when(invoiceItemService.getInvoiceById(1L)).thenReturn(invoiceItemDto);
        mokmvc.perform(get("/invoiceitem/1"))
                .andExpect(status().isOk());


    }


}
