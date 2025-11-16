package com.example.demo.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "should fullname not blank")
    @Column(name = "full_name",nullable = false)
    private String fullname;
    @Column(unique = true,nullable = false)
    private String email;
    @NotBlank(message = "should phone not blank")
    @Column(name = "phone_number",nullable = false)
    private String phone;
    @NotBlank(message = "should gender not blank")
    private String gender;
    @NotNull(message = "should birthdate not blank ")
    @Column(name = "birth_date",nullable = false)
    private LocalDate birthdate;
    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt=LocalDateTime.now();

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments=new ArrayList<>();

}
