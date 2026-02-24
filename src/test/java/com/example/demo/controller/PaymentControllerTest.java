package com.example.demo.controller;

import com.example.demo.dto.PaymentDto;
import com.example.demo.entity.Payment;
import com.example.demo.exceptionhandler.InvoiceNotFoundException;
import com.example.demo.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PaymentController.class) // Spring will create bean for you automatically
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentDto paymentDto;


    @Test
    void createPayment_ShouldReturnOk() throws Exception {

        paymentDto = new PaymentDto();
        paymentDto.setAmount(BigDecimal.valueOf(500));


        when(paymentService.createPayment(any(PaymentDto.class))).thenReturn(paymentDto);


        mockMvc.perform(post("/payment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(500));


    }


    @Test
    void createPayment_ShouldReturnNotFound_WhenInvoiceNotFound() throws Exception {

        paymentDto = new PaymentDto();
        paymentDto.setAmount(BigDecimal.valueOf(500));
        paymentDto.setInvoiceId(11L);


        when(paymentService.createPayment(any(PaymentDto.class))).thenThrow(new InvoiceNotFoundException("Not Found invoice with id " + 11));


        mockMvc.perform(post("/payment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not Found invoice with id " + 11));

    }


    @Test
    void getPaymentByInvoiceId_ShouldReturnPayment() throws Exception {


        when(paymentService.getPaymentByInvoiceId(99L)).thenReturn(paymentDto);

        mockMvc.perform(get("/payment/invoiceId/99"))
                .andExpect(status().isOk());


    }


    @Test
    void updatePayment_ShouldReturnUpdatePayment() throws Exception {

        paymentDto = new PaymentDto();
        paymentDto.setAmount(BigDecimal.valueOf(500));


        when(paymentService.updatePayment(eq(99L), any(PaymentDto.class))).thenReturn(paymentDto);

        mockMvc.perform(put("/payment/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(500));

    }


    @Test
    void getPaymentById_ShouldReturnPayment() throws Exception {

        paymentDto = new PaymentDto();
        paymentDto.setAmount(BigDecimal.valueOf(500));
        paymentDto.setStatus(Payment.PaymentStatus.SUCCESS);

        when(paymentService.getPaymentWithId(99L)).thenReturn(paymentDto);

        mockMvc.perform(get("/payment/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));

    }


    @Test
    void getAllPayments_shouldReturnAllPayments() throws Exception {

        paymentDto = new PaymentDto();
        paymentDto.setAmount(BigDecimal.valueOf(500));
        paymentDto.setStatus(Payment.PaymentStatus.SUCCESS);


        when(paymentService.getAllPayment()).thenReturn(List.of(paymentDto));

        mockMvc.perform(get("/payment"))
                .andExpect(status().isOk());


    }

    @Test
    void DeletePayment_ShouldDeletePayment() throws Exception {
        paymentDto = new PaymentDto();
        paymentDto.setAmount(BigDecimal.valueOf(500));

        doNothing().when(paymentService).deletePayment(1L);
        mockMvc.perform(delete("/payment/1"))
                .andExpect(status().isOk());

    }

}
