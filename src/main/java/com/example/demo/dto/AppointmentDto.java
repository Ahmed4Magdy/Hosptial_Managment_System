package com.example.demo.dto;

import com.example.demo.entity.Appointment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentDto {

    private Long id;
    @NotNull
    private Long patientId;
    @NotNull
    private Long doctorId;
    @NotNull
    private LocalDateTime appointmentDateTime;
    @Column(nullable = false)
    private Appointment.Status status = Appointment.Status.SCHEDULED;
    //    @NotBlank(message = "should notes not blank")
    private String notes;


}
