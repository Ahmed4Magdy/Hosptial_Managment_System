package com.example.demo.repository;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@DataJpaTest
public class MedicalRepositoryTest {


    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;


    private Prescription prescription;
    private Appointment appointment;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime datetime;

    @BeforeEach
    void setup() {

        datetime = LocalDateTime.of(2025, 11, 15, 9, 30);


        doctor = new Doctor();
        doctor.setEmail("ahmed@gmail.com");
        doctor.setFullName("ahmed magdy");
        doctor.setPhoneNumber("00i329802");
        doctor.setSpecialization("cardio");


        patient = new Patient();
        patient.setEmail("ahmed@gmail.com");
        patient.setFullname("ahmed magdy");
        patient.setGender("MALE");
        patient.setPhone("23696238359");
        patient.setBirthdate(LocalDate.of(2002, 4, 14));

        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(datetime);
        appointment.setStatus(Appointment.Status.COMPLETED);

    }

    @Test
    void test_getAllprescriptionByPatientId() {

        doctorRepository.save(doctor);
        patientRepository.save(patient);
        appointmentRepository.save(appointment);



    }


}
