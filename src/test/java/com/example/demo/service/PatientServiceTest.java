package com.example.demo.service;

import com.example.demo.dto.PatientDto;
import com.example.demo.entity.Patient;
import com.example.demo.mapper.PatientMapper;
import com.example.demo.repository.PatientRepository;
import com.example.demo.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {


    @Mock
    private PatientRepository patientRepository;
    @Mock
    private PatientMapper patientMapper;


    @InjectMocks
    private PatientServiceImpl patientServiceimpl;


    private Patient patient;
    private PatientDto dto;

    @BeforeEach
    void setup() {

        patient = new Patient();
        patient.setEmail("ahmed@gmail.com");
        patient.setBirthdate(LocalDate.parse("2002-04-14"));

        dto = new PatientDto();
        dto.setEmail("ahmed@gmail.com");
        dto.setBirthdate(LocalDate.parse("2002-04-14"));
    }

    @Test
    public void test_create_patient() {

        when(patientMapper.toEntity(dto)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toDto(patient)).thenReturn(dto);


        PatientDto result = patientServiceimpl.createpatient(dto);
        assertNotNull(result);

    }


    @Test
    void test_update_patient() {
        Long id = patient.getId();

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        doNothing().when(patientMapper).updatepatienttodto(dto, patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toDto(patient)).thenReturn(dto);
        PatientDto result = patientServiceimpl.update(id, dto);


    }


    @Test
    void test_get_patient_with_id() {
        Long id = patient.getId();

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(patient)).thenReturn(dto);

        PatientDto result = patientServiceimpl.findPatientById(id);
    }


    @Test
    void test_delete_patient() {
        Long id = patient.getId();
        doNothing().when(patientRepository).deleteById(id);

        patientServiceimpl.delete(id);

        verify(patientRepository, times(1)).deleteById(id);


    }


}
