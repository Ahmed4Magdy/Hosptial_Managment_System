package com.example.demo.repository;

import com.example.demo.entity.Patient;
import com.example.demo.exceptionhandler.PatientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PatientRepositoryTest {


    @Autowired
    private PatientRepository patientRepository;

    private Patient patient;

    @BeforeEach
    public void setup() {

        patient = new Patient();
        patient.setEmail("ahmed@gmail.com");
        patient.setFullname("ahmed magdy");
        patient.setGender("MALE");
        patient.setPhone("23696238359");
        patient.setBirthdate(LocalDate.of(2002, 4, 14));
        patientRepository.save(patient);


    }


    @Test
    public void test_create_Patient() {

        boolean exist = patientRepository.existsByEmail("ahmed@gmail.com");

    }


    @Test
    public void test_update_patient() {
        Long id = patient.getId();

        Patient exist = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("not found patient with " + id));
        exist.setFullname("tata");
        patient = patientRepository.save(exist);
        assertThat(exist.getFullname()).isEqualTo("tata");

    }

    @Test
    public void test_finalAllPatient() {

        List<Patient> patient = patientRepository.findAll();
        assertThat(patient).hasSize(1);

    }
}
