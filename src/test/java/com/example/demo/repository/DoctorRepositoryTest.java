package com.example.demo.repository;

import com.example.demo.entity.Doctor;
import com.example.demo.exceptionhandler.DoctorNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DoctorRepositoryTest {


    @Autowired
    private DoctorRepository doctorRepository;

    private Doctor doctor;

    @BeforeEach
    void setup() {

        doctor = new Doctor();
        doctor.setEmail("ahmed@gmail.com");
        doctor.setFullName("ahmed magdy");
        doctor.setPhoneNumber("00i329802");
        doctor.setSpecialization("cardio");

        doctorRepository.save(doctor);


    }


    @Test
    public void test_create_Doctor() {

        boolean exist = doctorRepository.existsByEmail("ahmed@gmail.com");

    }

    @Test
    public void test_update_Doctor() {
        Long id = doctor.getId();

        Doctor exist = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException("not found doctor with " + id));
        exist.setFullName("ahmed magdy");
        doctor = doctorRepository.save(exist);
        assertThat(exist.getFullName()).isEqualTo("ahmed magdy");

    }

    @Test
    public void test_finalAllPatient() {

        List<Doctor> doctor = doctorRepository.findAll();
        assertThat(doctor).hasSize(1);

    }


}
