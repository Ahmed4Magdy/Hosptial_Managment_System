package com.example.demo.repository;


import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;
import com.example.demo.exceptionhandler.AppointmentNotFoundException;
import com.example.demo.exceptionhandler.DoctorNotFoundException;
import com.example.demo.exceptionhandler.PatientNotFoundException;
import com.example.demo.exceptionhandler.PrescriptionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class PrescriptionRepositoryTest {


    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    private Prescription prescription;
    private Appointment appointment;
    private Appointment appointment1;
    private Doctor doctor;
    private Doctor doctor1;
    private Patient patient;
    private Patient patient1;
    private LocalDateTime datetime;

    @BeforeEach
    void setup() {

        datetime = LocalDateTime.of(2025, 11, 15, 9, 30);


        doctor = new Doctor();
        doctor.setEmail("ahmed@gmail.com");
        doctor.setFullName("ahmed magdy");
        doctor.setPhoneNumber("00i329802");
        doctor.setSpecialization("cardio");
        doctorRepository.save(doctor);

        doctor1 = new Doctor();
        doctor1.setEmail("tata@gmail.com");
        doctor1.setFullName("tata magdy");
        doctor1.setPhoneNumber("00i329802");
        doctor1.setSpecialization("cardio");
        doctorRepository.save(doctor1);

        patient = new Patient();
        patient.setEmail("ahmed@gmail.com");
        patient.setFullname("ahmed magdy");
        patient.setGender("MALE");
        patient.setPhone("23696238359");
        patient.setBirthdate(LocalDate.of(2002, 4, 14));
        patientRepository.save(patient);

        patient1 = new Patient();
        patient1.setEmail("ashraf@gmail.com");
        patient1.setFullname("ashraf magdy");
        patient1.setGender("MALE");
        patient1.setPhone("23696238359");
        patient1.setBirthdate(LocalDate.of(2002, 4, 14));
        patientRepository.save(patient1);

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(datetime);
        appointment.setStatus(Appointment.Status.SCHEDULED);
        appointmentRepository.save(appointment);


        appointment1 = new Appointment();
        appointment1.setDoctor(doctor1);
        appointment1.setPatient(patient1);
        appointment1.setAppointmentDateTime(datetime);
        appointment1.setStatus(Appointment.Status.SCHEDULED);
        appointmentRepository.save(appointment1);



        prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
        prescription.setDiagnosis("kook");
        prescription.setMedications("vitamine");
        prescription.setStatus(Prescription.Status.ACTIVE);
        prescriptionRepository.save(prescription);


    }


    @Test
    void test_createPrescription() {


        Appointment existing1 = appointmentRepository.findById(appointment.getId()).orElseThrow(() ->
                new AppointmentNotFoundException("Not Found Appointment with " + appointment.getId()));
        Doctor existing2 = doctorRepository.findById(doctor.getId()).orElseThrow(() -> new DoctorNotFoundException("Not Found Doctor with " + doctor.getId()));
        Patient existing3 = patientRepository.findById(patient.getId()).orElseThrow(() -> new PatientNotFoundException("Not Found Patient with " + patient.getId()));


        prescriptionRepository.save(prescription);


    }


    @Test
    void test_updatePrescription() {


        Prescription existing = prescriptionRepository.findById(prescription.getId()).orElseThrow(() -> new PrescriptionNotFoundException("Prescription Not Found With " + prescription.getId()));
        Appointment existing1 = appointmentRepository.findById(appointment1.getId()).orElseThrow(() -> new AppointmentNotFoundException("Not Found Appointment with " + appointment.getId()));
        Doctor existing2 = doctorRepository.findById(doctor1.getId()).orElseThrow(() -> new DoctorNotFoundException("Not Found Doctor with " + doctor.getId()));
        Patient existing3 = patientRepository.findById(patient1.getId()).orElseThrow(() -> new PatientNotFoundException("Not Found Patient with " + patient.getId()));

        existing.setDoctor(existing2);
        existing.setPatient(existing3);
        existing.setAppointment(existing1);
        Prescription updated = prescriptionRepository.save(existing);


        assertThat(updated.getDoctor().getEmail()).isEqualTo("tata@gmail.com");


    }

    @Test
    void test_getPrescriptionId() {


        Prescription existing = prescriptionRepository.findById(prescription.getId()).orElseThrow(() -> new PrescriptionNotFoundException("Prescription Not Found With " + prescription.getId()));
        assertThat(existing).isNotNull();

    }


    @Test
    void test_getAllPrescription() {


        List<Prescription> prescriptions = prescriptionRepository.findAll();
        assertThat(prescriptions).hasSize(1);

    }


    @Test
    void test_getAllPrescription_forPatientId_And_Status() {

        Patient exist = patientRepository.findById(patient.getId()).orElseThrow(() -> new PatientNotFoundException("Not Found Patient with" + patient.getId()));

        List<Prescription> prescriptions = prescriptionRepository.findByPatientIdAndStatus(patient.getId(), prescription.getStatus());

        assertThat(prescriptions).hasSize(1);

    }


    @Test
    void test_getAllPrescription_forPatientId_And_Status_Conflict() {


        Patient exist = patientRepository.findById(patient.getId()).orElseThrow(() -> new PatientNotFoundException("Not Found Patient with" + patient.getId()));

        // المفروض هنا المريض واحد مش موجود ف الروشته ال حفظناها والموجود المريض بدون رقم فلازم اشيل مريض واحد علشان مش هيلاقي ف الداتابيز
        List<Prescription> prescriptions = prescriptionRepository.findByPatientIdAndStatus(patient1.getId(), prescription.getStatus());

        assertThat(prescriptions).hasSize(0);

    }


    @Test
    void test_getAllPrescription_forDoctorId_And_Status() {

        Doctor exist = doctorRepository.findById(doctor.getId()).orElseThrow(() -> new DoctorNotFoundException("Not Found doctor with" + doctor.getId()));

        List<Prescription> prescriptions = prescriptionRepository.findByPatientIdAndStatus(doctor.getId(), prescription.getStatus());

        assertThat(prescriptions).hasSize(1);

    }


    @Test
    void test_getAllPrescription_forDoctorId_And_Status_Conflict() {

        Doctor exist = doctorRepository.findById(doctor.getId()).orElseThrow(() -> new DoctorNotFoundException("Not Found doctor with" + doctor.getId()));

        List<Prescription> prescriptions = prescriptionRepository.findByPatientIdAndStatus(doctor1.getId(), prescription.getStatus());

        assertThat(prescriptions).hasSize(0);

    }


    @Test
    void test_getAllPrescriptionByStatus() {

        List<Prescription> prescriptions = prescriptionRepository.findByStatus(prescription.getStatus());

        assertThat(prescriptions).hasSize(1);

    }


    @Test
    void test_deletePrescription() {

        Prescription exist = prescriptionRepository.findById(prescription.getId()).orElseThrow(() -> new PrescriptionNotFoundException("Not Found Prescription with" + prescription.getId()));
        exist.setStatus(Prescription.Status.DELETED);
        prescriptionRepository.delete(exist);

    }


}
