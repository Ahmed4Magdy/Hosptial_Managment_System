package com.example.demo.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDto {



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

}
