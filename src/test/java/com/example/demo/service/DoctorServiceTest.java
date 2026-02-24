package com.example.demo.service;

import com.example.demo.dto.DoctorDto;
import com.example.demo.dto.PatientDto;
import com.example.demo.entity.Doctor;
import com.example.demo.exceptionhandler.DoctorNotFoundException;
import com.example.demo.exceptionhandler.DuplicateDoctorException;
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
import static org.junit.Assert.assertThrows;
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
    public void test_create_doctor_ShouldReturnDto() {
        when(doctorRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(doctorMapper.toEntity(dto)).thenReturn(doctor);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(dto);


        DoctorDto result = doctorServiceimpl.createdoctor(dto);
        assertNotNull(result);
        verify(doctorRepository, times(1)).existsByEmail(dto.getEmail());
        verify(doctorRepository, times(1)).save(doctor);
    }

    @Test
    public void test_create_doctor_ShouldThrowException() {
        when(doctorRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(DuplicateDoctorException.class, () -> {
            doctorServiceimpl.createdoctor(dto);
        });

        // this ensure that save method was never called on doctorRepository
        // بيتاكد ان الميثود سيف ال في الريبو مدخلتش اطلاقا باي اوبجكت
        verify(doctorRepository, never()).save(any());
    }


    @Test
    void test_updateDoctor_ShouldReturnUpdateDoctorDto() {
        Long id = doctor.getId();

        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        doNothing().when(doctorMapper).updateDoctorFromDto(dto, doctor);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(dto);
        DoctorDto result = doctorServiceimpl.update(id, dto);

        assertNotNull(result);
        verify(doctorRepository, times(1)).save(any());
        verify(doctorMapper,times(1)).updateDoctorFromDto(dto,doctor);

    }


    @Test
    void test_updateDoctor_shouldThrowException_whenDoctorNotFoundException() {
        Long id = doctor.getId();

        when(doctorRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(DoctorNotFoundException.class, () -> {
            doctorServiceimpl.update(id, dto);
        });

        verify(doctorRepository, never()).save(any());
        verify(doctorMapper, never()).updateDoctorFromDto(any(), any());

    }




    @Test
    void test_getDoctorById_shouldReturnDoctor() {
        Long id = doctor.getId();

        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(dto);

        DoctorDto result = doctorServiceimpl.findDoctorById(id);

        assertNotNull(result);
    }

    @Test
    void test_getDoctorById_shouldReturnThrowException_whenNotFound() {
        Long id = doctor.getId();

        when(doctorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class,()->{
            doctorServiceimpl.findDoctorById(id);
        });
        verify(doctorMapper,never()).toDto(any());
    }


    @Test
    void test_delete_Doctor() {
        Long id = doctor.getId();
        doNothing().when(doctorRepository).deleteById(id);

        doctorServiceimpl.deleteDoctor(id);

        verify(doctorRepository, times(1)).deleteById(id);


    }


}
