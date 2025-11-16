package com.example.demo.controller;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.entity.Appointment;
import com.example.demo.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(MockitoExtension.class)
@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AppointmentService appointmentService;
    @Autowired
    private ObjectMapper objectMapper;

    private AppointmentDto dto;

    @BeforeEach
    void setup() {

        dto = new AppointmentDto();
        dto.setDoctorId(1L);
        dto.setPatientId(1L);
        dto.setStatus(Appointment.Status.SCHEDULED);
        dto.setAppointmentDateTime(LocalDateTime.of(2025, 11, 16, 8, 30));

    }

    @Test
    void test_Create_appointmet() throws Exception {

        when(appointmentService.createAppointment(any(AppointmentDto.class))).thenReturn(dto);

        mockMvc.perform(post("/appointment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))

                .andExpect(status().isCreated());


    }


    @Test
    void testUpdateAppointment() throws Exception {

        when(appointmentService.update(eq(1L), any(AppointmentDto.class))).thenReturn(dto);

        mockMvc.perform(put("/appointment/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))

                .andExpect(status().isOk());


    }


    @Test
    void testCancelAppointment() throws Exception {

//        AppointmentDto dto1 = new AppointmentDto();
//        dto1.setStatus(Appointment.Status.CANCELED);
        when(appointmentService.cancelAppointment(1L)).thenReturn(dto);
        mockMvc.perform(get("/appointment/cancel/1"))
                .andExpect(status().isOk());


    }


    @Test
    void testCompleteAppointment() throws Exception {

//        AppointmentDto dto1 = new AppointmentDto();
//        dto1.setStatus(Appointment.Status.COMPLETED);
        when(appointmentService.compeleteAppointment(1L)).thenReturn(dto);
        mockMvc.perform(get("/appointment/complete/1"))
                .andExpect(status().isOk());


    }


    @Test
    void testGetAppointmentById() throws Exception {

        when(appointmentService.getAppointmentById(1L)).thenReturn(dto);

        mockMvc.perform(get("/appointment/getappointmentById/1"))
                .andExpect(status().isOk());

    }


    @Test
    void testGetAllAppointment() throws Exception {

        when(appointmentService.getAllAppointment()).thenReturn(List.of(dto));

        mockMvc.perform(get("/appointment"))
                .andExpect(status().isOk());

    }

    @Test
    void testGetDoctorAppointmentsByDate() throws Exception {

        when(appointmentService.getAllDoctorIdAndAppointment(eq(1L), eq(LocalDate.of(2025, 11, 15)))).thenReturn(List.of(dto));

        mockMvc.perform(get("/appointment/getdoctorData/1")
                        .param("date", "2025-11-18"))

                .andExpect(status().isOk());


    }


    @Test
    void testGetPatientAppointmentsByDate() throws Exception {

        when(appointmentService.getAllPatientIdAndAppointment(eq(1L), eq(LocalDate.of(2025, 11, 15)))).thenReturn(List.of(dto));

        mockMvc.perform(get("/appointment/getpatientData/1")
                        .param("date", "2025-11-18"))

                .andExpect(status().isOk());


    }


    @Test
    void testDeleteAppointment() throws Exception {
        Mockito.doNothing().when(appointmentService).deleteAppointmentById(1L);

        mockMvc.perform(delete("/appointment/1"))
                .andExpect(status().isOk());
    }


}
