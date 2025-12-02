package com.example.demo.dto;

import com.example.demo.entity.Doctor;
import com.example.demo.entity.Prescription;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrescriptionResponseDto {

    private Long id;

    private DoctorDto doctor;

    private String diagnosis;

    private PrescriptionStatus status;



}
