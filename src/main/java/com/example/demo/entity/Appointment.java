package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @NotNull
    @JsonIgnore
    private Doctor doctor;
    @NotNull
    private LocalDateTime appointmentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.SCHEDULED; // default value

    public enum Status {
        SCHEDULED,
        CANCELED,
        COMPLETED
    }

    //    @NotBlank(message = "should notes not blank")
    private String notes;

    @OneToMany(mappedBy = "appointment")
    @JsonIgnore
    private List<Prescription> prescription;

}
