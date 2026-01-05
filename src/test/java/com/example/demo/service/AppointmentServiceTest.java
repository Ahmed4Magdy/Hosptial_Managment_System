package com.example.demo.service;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.exceptionhandler.*;
import com.example.demo.mapper.AppointmentMapper;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.RuntimeErrorException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {


    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentServiceimpl;


    private Appointment appointment;
    private AppointmentDto dto;
    private Appointment appointment1;
    private Doctor doctor;
    private Patient patient;

    @BeforeEach
    void setup() {

        doctor = new Doctor();
        doctor.setId(1L);

        patient = new Patient();
        patient.setId(1L);



    }

    @Test
    void test_CreateAppointment_ShouldThrowDoctorNotAvailableException_WhenConfict() {

        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));


        dto = new AppointmentDto();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        dto.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));



        when(appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusIn(1L, dto.getAppointmentDateTime(), List.of(Appointment.Status.SCHEDULED, Appointment.Status.COMPLETED))).thenReturn(true);

        DoctorNotAvaliableException exception = assertThrows(DoctorNotAvaliableException.class, () -> {
            appointmentServiceimpl.createAppointment(dto);
        });

        assertEquals("Doctor is not available at this time or completed.", exception.getMessage());

    }


    @Test
    void test_CreateAppointment_ShouldThrowPatientNotAvailableException_WhenConflict() {
        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));

        dto = new AppointmentDto();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        dto.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));


        when(appointmentRepository.existsByPatientIdAndAppointmentDateTimeAndStatusIn(1L, dto.getAppointmentDateTime(), List.of(Appointment.Status.SCHEDULED, Appointment.Status.COMPLETED))).thenReturn(true);

        PatientNotAvaliableException exception = assertThrows(PatientNotAvaliableException.class, () -> {
            appointmentServiceimpl.createAppointment(dto);
        });

        assertEquals("You already have an appointment at this time or completed.", exception.getMessage());

    }


    @Test
    void createAppointment_ShouldSaveSuccessfully() {

        dto = new AppointmentDto();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        dto.setAppointmentDateTime(LocalDateTime.of(2026, 11, 20, 22, 30));


        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(LocalDateTime.of(2026, 11, 20, 22, 30));



        when(appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusIn(1L, dto.getAppointmentDateTime(), List.of(Appointment.Status.SCHEDULED, Appointment.Status.COMPLETED))).thenReturn(false);

        when(appointmentRepository.existsByPatientIdAndAppointmentDateTimeAndStatusIn(1L, dto.getAppointmentDateTime(), List.of(Appointment.Status.SCHEDULED, Appointment.Status.COMPLETED))).thenReturn(false);


        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentMapper.toEntity(dto)).thenReturn(appointment);
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toDto(appointment)).thenReturn(dto);


        AppointmentDto result = appointmentServiceimpl.createAppointment(dto);

        assertNotNull(result);


    }


    @Test
    void test_updateAppointment_ShouldThrow_WhenNotFound() {

        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));

        dto = new AppointmentDto();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        dto.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));


        when(appointmentRepository.findById(2L)).thenReturn(Optional.empty());

        AppointmentNotFoundException exception = assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentServiceimpl.update(2L, dto);
        });

        assertEquals("Not Found appointment with" + 2, exception.getMessage());

    }

    @Test
    void updateAppointment_ShouldUpdateDoctorAndPatientAndDate() {
        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));

        dto = new AppointmentDto();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        dto.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));


        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        doNothing().when(appointmentMapper).updateAppointmentfromDto(dto, appointment);
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toDto(appointment)).thenReturn(dto);

        AppointmentDto result = appointmentServiceimpl.update(1L, dto);

        assertEquals(result, dto);
        verify(appointmentMapper, times(1)).updateAppointmentfromDto(dto, appointment);
    }


    @Test
    void test_CancelAppointmet_ShouldCancelAppointmet() {

        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(Appointment.Status.SCHEDULED);
        appointment.setAppointmentDateTime(LocalDateTime.of(2026, 12, 27, 22, 30));

        dto = new AppointmentDto();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        appointment.setAppointmentDateTime(LocalDateTime.of(2026, 12, 27, 22, 30));



        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toDto(appointment)).thenReturn(dto);
        AppointmentDto result = appointmentServiceimpl.cancelAppointment(1L);


    }

    //  خد باللك هنا ممكن يديم ايرور عادي جدا بسبب اختلاف المواعيد المفروض اشوف امتي هيديني اكسبشن واظبط ساعتها المواعيد علشان يطلع صح يعني المفروض اللوجيك بيقول لو المعاد الحالي اكبر من ال موجود عندك هتقولي ان المعاد ف الماضي ومش هعرف اكنسله لانه عدا ف المفروض اعمل اي بقي اظبط المعاد ال عندي واخليه انتهي او اقل من المعاد ال عندي الحالي علشان تظبط

    @Test
    void test_CancelAppointmet_ShouldThrow_WhenTimePassed() {

        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(LocalDateTime.of(2025, 11, 15, 22, 30));


        dto = new AppointmentDto();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        dto.setAppointmentDateTime(LocalDateTime.of(2025, 11, 25, 22, 30));


        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        // لازم الوقت الحالي يكون اكبر من ال موجود ف ال beforeeach علشان يرمي الاكسبشن غير كده مش هيرمي لان ده مش مطابق للسيرفس وغير كده اشتغلت ازاي عباره عن لما رنيت التيست بيروح للسيرفس وال ليها فانكشن اسمها كانسل ومعاه الاي دي واحد هو واخده من ال انا حاطه ليه وبعدها بيكون معاه كل الداتا والمعاد ال هو قارن بيه
        CannotCancelPastAppointmentException exception = assertThrows(CannotCancelPastAppointmentException.class, () -> {
            appointmentServiceimpl.cancelAppointment(1L);
        });

        assertEquals("cannot cancel a past appointment", exception.getMessage());


    }


    //  خد باللك هنا ممكن يديك ايرور عادي جدا بسبب اختلاف المواعيد المفروض اشوف امتي هيديني اكسبشن واظبط ساعتها المواعيد علشان يطلع صح يعني المفروض اللوجيك بيقول لو المعاد الحالي اقل  من ال موجود عندك هتقولي ان المعاد مش هقدر اكمله غير لما يعدي الوفت بتاعه او ينتهي ف المفروض اعمل اي بقي اظبط المعاد ال عندي واخليه انتهي او اقل من المعاد ال عندي الحالي علشان تظبط
    @Test
    void test_CompleteAppointmet_ShouldCompleteAppointmet() {

        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(LocalDateTime.of(2025, 11, 15, 22, 30));

        dto = new AppointmentDto();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        dto.setAppointmentDateTime(LocalDateTime.of(2025, 11, 15, 22, 30));


        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Appointment saved = new Appointment();
//        saved.setStatus(Appointment.Status.COMPLETED);
        when(appointmentRepository.saveAndFlush(appointment)).thenReturn(saved);
        when(appointmentMapper.toDto(saved)).thenReturn(dto);
        AppointmentDto result = appointmentServiceimpl.compeleteAppointment(1L);

    }
    //  خد باللك هنا ممكن يديك ايرور عادي جدا بسبب اختلاف المواعيد المفروض اشوف امتي هيديني اكسبشن واظبط ساعتها المواعيد علشان يطلع صح يعني المفروض اللوجيك بيقول لو المعاد الحالي اقل  من ال موجود عندك  يعني  المعاد لسه مجاش او عدا علشان اقدر اعمله كومبيليت فلازم الوقت ال حاطه عندك يكون ف المستقبل والحالي يكون اقل منه يعني لو هو يوم 5 وانا يوم 4 مش هينفع لانه لسه مجاش فلازم حاجه زي كدا اظبطها بردو

    @Test
    void test_CompleteAppointmet_ShouldThrow_WhenTimeBefore() {

        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(LocalDateTime.of(2026, 12, 27, 22, 30));

        dto = new AppointmentDto();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        dto.setAppointmentDateTime(LocalDateTime.of(2026, 12, 27, 22, 30));


        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        CannotcompleteaappointmentbeforefinishedException exception = assertThrows(CannotcompleteaappointmentbeforefinishedException.class, () -> {
            appointmentServiceimpl.compeleteAppointment(1L);
        });

        assertEquals("cannot complete a appointment before finished", exception.getMessage());


    }

    @Test
    void test_Should_getDoctorId_And_Appointment() {

        dto = new AppointmentDto();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        dto.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));


        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));


        LocalDate date = LocalDate.of(2025, 11, 20);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        when(appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(1L, start, end)).thenReturn(List.of(appointment));
        when(appointmentMapper.toDto(appointment)).thenReturn(dto);
        List<AppointmentDto> appointments = appointmentServiceimpl.getAllDoctorIdAndAppointment(1L, date);


    }

    @Test
    void test_Should_getPatientId_And_Appointment() {

        dto = new AppointmentDto();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        dto.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));



        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));


        LocalDate date = LocalDate.of(2025, 11, 20);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        when(appointmentRepository.findByPatientIdAndAppointmentDateTimeBetween(1L, start, end)).thenReturn(List.of(appointment));
        when(appointmentMapper.toDto(appointment)).thenReturn(dto);
        List<AppointmentDto> appointments = appointmentServiceimpl.getAllPatientIdAndAppointment(1L, date);


    }


    @Test
    void test_getAppointmentById() {

        dto = new AppointmentDto();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        dto.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));


        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));


        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toDto(appointment)).thenReturn(dto);

        AppointmentDto result = appointmentServiceimpl.getAppointmentById(1L);

        assertEquals(result, dto);

    }

    @Test
    void test_getAllAppointment() {

        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));

        dto = new AppointmentDto();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        dto.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));


        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));
        when(appointmentMapper.toDto(appointment)).thenReturn(dto);
        List<AppointmentDto> result = appointmentServiceimpl.getAllAppointment();

    }


    @Test
    void test_deleteAppointmentById() {


        doNothing().when(appointmentRepository).deleteById(1L);
        appointmentServiceimpl.deleteAppointmentById(1L);
        verify(appointmentRepository, times(1)).deleteById(1L);

    }


}
