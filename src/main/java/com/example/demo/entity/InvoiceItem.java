package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "should serviceName not blank")
    private String serviceName;
    private BigDecimal price;
    private int quantity=1;
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;



}
