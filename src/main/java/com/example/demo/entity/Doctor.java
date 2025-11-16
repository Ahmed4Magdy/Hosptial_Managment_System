package com.example.demo.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "should fullname not blank")
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @NotBlank(message = "should specialization not blank")
    @Column(name = "specialization", nullable = false)
    private String specialization;
    @Column(unique = true, nullable = false)
    private String email;
    @NotBlank(message = "should phoneNumber not blank")
    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;
    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments=new ArrayList<>();


}
