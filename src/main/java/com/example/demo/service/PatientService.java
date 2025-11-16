package com.example.demo.service;

import com.example.demo.dto.PatientDto;
import com.example.demo.entity.Patient;

import java.util.List;

public interface PatientService {


    public PatientDto createpatient(PatientDto dto);

    public PatientDto update(Long id, PatientDto dto);

    public List<PatientDto> findAllPatient();

    public PatientDto findPatientById(Long id);

    public void delete(Long id);

    public PatientDto findByEmail(String email);

}
