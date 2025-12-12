package com.example.demo.dto;

import com.example.demo.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InvoiceDto {


    private Long id;


    private Long doctorId;

    @NotBlank(message = "should doctorname not blank")
    @Column(name = "doctor_name")
    private String doctorName;

    private Long patientId;


    @NotBlank(message = "should patientName not blank")
    @Column(name = "patient_name")
    private String patientName;


    private Long appointmentId;

    private Invoice.Status status = Invoice.Status.PENDING;





    List<InvoiceItemDto> items = new ArrayList<>();


}
