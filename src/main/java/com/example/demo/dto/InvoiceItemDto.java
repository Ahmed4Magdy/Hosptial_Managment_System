package com.example.demo.dto;

import com.example.demo.entity.Invoice;
import jakarta.persistence.Column;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InvoiceItemDto {


    private Long id;

    private Long invoiceId;

    @Column(name = "service_name")
    @NotBlank(message = "should serviceName not blank")
    private String serviceName;
    @NotBlank(message = "should price not blank")
    private BigDecimal price;
    @NotBlank(message = "should quantity not blank")
    private int quantity;
    @NotBlank(message = "should total not blank")
    private double total;



}
