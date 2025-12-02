package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicalRecordDto {
    private Long id;
    private String fullname;
    private List<AppointmentResponseDto> appointments;
    private Long totalPrescription;
    private List<PrescriptionResponseDto> prescriptions;

}
