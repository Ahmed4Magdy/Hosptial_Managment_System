package com.example.demo.dto;

import com.example.demo.entity.Payment;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentDto {

    private Long id;
    private Long invoiceId;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private Payment.PaymentStatus status;
    @Column(name = "change_amount")
    private BigDecimal changeAmount;

    @Column(nullable = false)
    private Payment.PaymentMethod method;
    private LocalDateTime paymentDate;

    // getters & setters
}
