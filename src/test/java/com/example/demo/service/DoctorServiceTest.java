package com.example.demo.service;

import com.example.demo.dto.DoctorDto;
import com.example.demo.dto.PatientDto;
import com.example.demo.entity.Doctor;
import com.example.demo.mapper.DoctorMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.service.impl.DoctorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.print.Doc;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {


    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private DoctorMapper doctorMapper;
    @InjectMocks
    private DoctorServiceImpl doctorServiceimpl;

    private Doctor doctor;
    private DoctorDto dto;

    @BeforeEach
    void setup() {

        doctor = new Doctor();
        doctor.setFullName("ahmed");
        doctor.setEmail("ahmed@gmail.com");
        doctor.setPhoneNumber("019898848");
        doctor.setSpecialization("cardio");

        dto = new DoctorDto();
        dto.setEmail("ahmed@gmail.com");
        dto.setFullName("ahmed");
        dto.setPhoneNumber("019898848");
        dto.setSpecialization("cardio");

    }


    @Test
    public void test_create_Doctor() {

        when(doctorMapper.toEntity(dto)).thenReturn(doctor);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(dto);


        DoctorDto result = doctorServiceimpl.createdoctor(dto);
        assertNotNull(result);

    }

    @Test
    void test_update_patient() {
        Long id = doctor.getId();

        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        doNothing().when(doctorMapper).updateDoctorFromDto(dto, doctor);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(dto);
        DoctorDto result = doctorServiceimpl.update(id, dto);

        assertNotNull(result);

    }


    @Test
    void test_get_patient_with_id() {
        Long id = doctor.getId();

        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(dto);

        DoctorDto result = doctorServiceimpl.findDoctorById(id);

        assertNotNull(result);
    }


    @Test
    void test_delete_patient() {
        Long id = doctor.getId();
        doNothing().when(doctorRepository).deleteById(id);

        doctorServiceimpl.deleteDoctor(id);

        verify(doctorRepository, times(1)).deleteById(id);


    }


}
