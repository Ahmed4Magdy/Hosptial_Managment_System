package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @NotBlank(message = "should doctorname not blank")
    private String doctorName;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotBlank(message = "should patientName not blank")
    @Column(name = "patient_name")
    private String patientName;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @OneToMany(mappedBy = "invoice",cascade = CascadeType.ALL)
    List<InvoiceItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private BigDecimal totalAmount;

    public enum Status {
        PENDING,
        PAID,
        CANCELED
    }

    @Column(name = "created_at")
    private LocalDateTime created_at = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime updated_at = LocalDateTime.now();

    @OneToOne(mappedBy = "invoice")
    private Payment payment;



}
