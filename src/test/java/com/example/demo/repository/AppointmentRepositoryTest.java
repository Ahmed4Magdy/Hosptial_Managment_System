package com.example.demo.repository;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.exceptionhandler.AppointmentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AppointmentRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;


    private Appointment appointment;
    private Doctor doctor1;
    private Doctor doctor2;
    private Patient patient1;
    private Patient patient2;
    private LocalDateTime dateTime;
    private LocalDate date;

    @BeforeEach
    void setup() {

        doctor1 = new Doctor();
        doctor1.setEmail("ahmed@gmail.com");
        doctor1.setFullName("ahmed magdy");
        doctor1.setPhoneNumber("878700000");
        doctor1.setSpecialization("cardio");
        doctorRepository.save(doctor1);


        doctor2 = new Doctor();
        doctor2.setEmail("zezo@gmail.com");
        doctor2.setFullName("zezo magdy");
        doctor2.setPhoneNumber("87878880");
        doctor2.setSpecialization("TB");
        doctorRepository.save(doctor2);

        patient1 = new Patient();
        patient1.setFullname("ashraf");
        patient1.setEmail("hamza@gmail");
        patient1.setPhone("00844848450");
        patient1.setBirthdate(LocalDate.of(2002, 4, 14));
        patient1.setGender("MALE");
        patientRepository.save(patient1);

        patient2 = new Patient();
        patient2.setFullname("tata");
        patient2.setEmail("tata@gmail");
        patient2.setPhone("0077748450");
        patient2.setBirthdate(LocalDate.of(2002, 4, 14));
        patient2.setGender("MALE");
        patientRepository.save(patient2);


    }


    @Test
    void test_ConflictExists() {

        dateTime = LocalDateTime.of(2025, 11, 15, 9, 30);

        date = LocalDate.of(2025, 11, 15);

        appointment = new Appointment();
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient1);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(Appointment.Status.SCHEDULED);

        appointmentRepository.saveAndFlush(appointment);

        boolean exist1 = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusIn(doctor1.getId(), dateTime, List.of(Appointment.Status.SCHEDULED, Appointment.Status.COMPLETED));
        boolean exist2 = appointmentRepository.existsByPatientIdAndAppointmentDateTimeAndStatusIn(patient1.getId(), dateTime, List.of(Appointment.Status.SCHEDULED, Appointment.Status.COMPLETED));

        assertThat(exist1).isTrue(); // ع اساس انه  موجود لو ترو يعني ده موجود
        assertThat(exist2).isTrue();


    }


    @Test
    void test_create_appointment_NoConflict() {
        dateTime = LocalDateTime.of(2025, 11, 15, 9, 30);

        date = LocalDate.of(2025, 11, 15);

        appointment = new Appointment();
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient1);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(Appointment.Status.SCHEDULED);
        appointmentRepository.save(appointment);
        boolean exist1 = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusIn(doctor2.getId(), dateTime, List.of(Appointment.Status.SCHEDULED, Appointment.Status.COMPLETED));
        boolean exist2 = appointmentRepository.existsByPatientIdAndAppointmentDateTimeAndStatusIn(patient2.getId(), dateTime, List.of(Appointment.Status.SCHEDULED, Appointment.Status.COMPLETED));
        assertThat(exist1).isFalse(); //
        assertThat(exist2).isFalse();


    }


    @Test
    void testConflictOnlyDoctor() {

        dateTime = LocalDateTime.of(2025, 11, 15, 9, 30);

        date = LocalDate.of(2025, 11, 15);

        appointment = new Appointment();
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient1);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(Appointment.Status.SCHEDULED);

        appointmentRepository.save(appointment);
// دي معناها بيقوللك لو ف نفس الدكتور وبنفس المعاد مش هينفع لانه مش هينفع الدكتور هيكون ف نفس المكان مرتين وكذللك المريض مينفعش يحجز لدكتورين ف نفس المعاد لان المريض هيبقي هنا وهناك ف نفس المعاد ازاي علشان كده بيقولو هل دا موجود ف بقوله اه علشان كده بيرحع ترو
        boolean exist1 = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusIn(doctor1.getId(), dateTime, List.of(Appointment.Status.SCHEDULED, Appointment.Status.COMPLETED));

        assertThat(exist1).isTrue();

    }


    @Test
    void testConflictOnlyPatient() {


        dateTime = LocalDateTime.of(2025, 11, 15, 9, 30);

        date = LocalDate.of(2025, 11, 15);

        appointment = new Appointment();
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient1);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(Appointment.Status.SCHEDULED);

        appointmentRepository.save(appointment);
// دي معناها بيقوللك لو ف نفس الدكتور وبنفس المعاد مش هينفع لانه مش هينفع الدكتور هيكون ف نفس المكان مرتين وكذللك المريض مينفعش يحجز لدكتورين ف نفس المعاد لان المريض هيبقي هنا وهناك ف نفس المعاد ازاي علشان كده بيقولو هل دا موجود ف بقوله اه علشان كده بيرحع ترو
        boolean exist = appointmentRepository.existsByPatientIdAndAppointmentDateTimeAndStatusIn(patient1.getId(), dateTime, List.of(Appointment.Status.SCHEDULED, Appointment.Status.COMPLETED));

        assertThat(exist).isTrue();

    }


    @Test
    void testUpdateAppointmentDoctorAndPatient() {

        dateTime = LocalDateTime.of(2025, 11, 15, 9, 30);

        date = LocalDate.of(2025, 11, 15);

        appointment = new Appointment();
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient1);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(Appointment.Status.SCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);

        saved.setPatient(patient2);
        saved.setDoctor(doctor2);
        saved.setAppointmentDateTime(LocalDateTime.of(2025, 11, 18, 9, 0));
        Appointment updated = appointmentRepository.save(saved);

        assertThat(updated.getDoctor().getId()).isEqualTo(doctor2.getId());
        assertThat(updated.getAppointmentDateTime()).isEqualTo(LocalDateTime.of(2025, 11, 18, 9, 0));
    }

    @Test
    void test_cancel_appointment_is_correct() {

        dateTime = LocalDateTime.of(2026, 12, 27, 10, 30);

        date = LocalDate.of(2025, 11, 18);

        appointment = new Appointment();
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient1);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(Appointment.Status.SCHEDULED);

        appointmentRepository.save(appointment);

        boolean canceled = LocalDateTime.now().isBefore(appointment.getAppointmentDateTime());

        assertThat(canceled).isTrue();

    }


    @Test
    void test_complete_appointment_is_correct() {

        dateTime = LocalDateTime.of(2025, 11, 10, 9, 30);

        date = LocalDate.of(2025, 11, 15);

        appointment = new Appointment();
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient1);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(Appointment.Status.SCHEDULED);

//        Appointment a = new Appointment();
//        a.setDoctor(doctor1);
//        a.setPatient(patient1);
//        a.setAppointmentDateTime(LocalDateTime.of(2025, 11, 15, 4, 30));
//        a.setStatus(Appointment.Status.SCHEDULED);
        appointmentRepository.save(appointment);

        boolean completed = LocalDateTime.now().isAfter(appointment.getAppointmentDateTime());

        assertThat(completed).isTrue();

    }

    @Test
    void testFindByDoctorIdAndAppointmentDateBetween() {

        dateTime = LocalDateTime.of(2025, 11, 10, 9, 30);

        date = LocalDate.of(2025, 11, 10);


        appointment = new Appointment();
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient1);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(Appointment.Status.COMPLETED);


        appointmentRepository.save(appointment);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(doctor1.getId(), start, end);

        assertThat(appointments).hasSize(1);

    }


    @Test
    void testFindByPatientIdAndAppointmentDateBetween() {

        dateTime = LocalDateTime.of(2025, 11, 10, 9, 30);

        date = LocalDate.of(2025, 11, 10);


        Appointment appointment1 = new Appointment();
        appointment1.setDoctor(doctor2);
        appointment1.setPatient(patient1);
        appointment1.setAppointmentDateTime(dateTime);
        appointment1.setStatus(Appointment.Status.SCHEDULED);

        appointmentRepository.save(appointment1);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentRepository.findByPatientIdAndAppointmentDateTimeBetween(patient1.getId(), start, end);

        assertThat(appointments).hasSize(1);

    }


    @Test
    void test_FindAppointmentById() {

        dateTime = LocalDateTime.of(2025, 11, 10, 9, 30);

        date = LocalDate.of(2025, 11, 10);


        appointment = new Appointment();
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient1);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(Appointment.Status.COMPLETED);

        appointmentRepository.save(appointment);

        Long id = appointment.getId();
        Appointment found = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException("Not Found appointment with " + id));
        assertThat(found).isNotNull();

    }


    @Test
    void test_FindAllAppointment() {

        dateTime = LocalDateTime.of(2025, 11, 10, 9, 30);

        date = LocalDate.of(2025, 11, 10);


        appointment = new Appointment();
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient1);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(Appointment.Status.COMPLETED);
        appointmentRepository.save(appointment);

        List<Appointment> appointments = appointmentRepository.findAll();
        assertThat(appointments).hasSize(1);

    }

    @Test
    void test_DeleteAppointment() {

        dateTime = LocalDateTime.of(2025, 11, 10, 9, 30);

        date = LocalDate.of(2025, 11, 10);


        appointment = new Appointment();
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient1);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(Appointment.Status.SCHEDULED);
        Appointment saved = appointmentRepository.save(appointment);
        Long id = saved.getId();
        appointmentRepository.deleteById(id);


    }

}



