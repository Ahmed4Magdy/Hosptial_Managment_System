package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatientDto {

    private String fullname;
    private String email;
    private String phone;
    private String gender;
    private LocalDate birthdate;

}
