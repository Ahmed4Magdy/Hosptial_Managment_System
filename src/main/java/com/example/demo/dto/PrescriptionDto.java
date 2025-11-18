package com.example.demo.dto;


import com.example.demo.entity.Prescription;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrescriptionDto {



    private Long appointmentId;

    @NotNull
    @Column(nullable = false)
    private Long patientId;


    @NotNull
    @Column(nullable = false)
    private Long doctorId;

    @NotBlank(message = "should diagnosis not blank")
    @Column(nullable = false)
    private String diagnosis;

    @NotBlank(message = "should medications not blan")
    @Column(nullable = false)
    private String medications;

    private String instructions;
    @Column(nullable = false)
    private Prescription.Status status = Prescription.Status.ACTIVE;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;


}
