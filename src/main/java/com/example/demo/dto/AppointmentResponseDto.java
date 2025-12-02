package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentResponseDto {
    private Long id;
    private LocalDateTime appointmentDateTime;
    private DoctorDto doctor;
}

