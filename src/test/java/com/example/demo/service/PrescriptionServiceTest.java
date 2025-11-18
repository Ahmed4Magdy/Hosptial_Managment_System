package com.example.demo.service;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.dto.DoctorDto;
import com.example.demo.dto.PatientDto;
import com.example.demo.dto.PrescriptionDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;
import com.example.demo.exceptionhandler.AppointmentNotFoundException;
import com.example.demo.exceptionhandler.PrescriptionNotFoundException;
import com.example.demo.mapper.PrescriptionMapper;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PrescriptionRepository;
import com.example.demo.service.impl.PrescriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;
    @Mock
    private PrescriptionMapper prescriptionMapper;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;
    @InjectMocks
    private PrescriptionServiceImpl prescriptionServiceimpl;


    private Prescription prescription;
    private PrescriptionDto prescriptionDto;
    private Doctor doctor;
    private DoctorDto doctorDto;
    private Patient patient;
    private PatientDto patientDto;
    private Appointment appointment;
    private AppointmentDto appointmentDto;

    @BeforeEach
    void setup() {

        doctor = new Doctor();
        doctor.setId(1L);

        patient = new Patient();
        patient.setId(1L);

        appointment = new Appointment();
        appointment.setId(1L);


        prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
        prescription.setDiagnosis("kook");
        prescription.setMedications("vitamine");
        prescription.setStatus(Prescription.Status.ACTIVE);
        prescriptionRepository.save(prescription);

        prescriptionDto = new PrescriptionDto();
        prescriptionDto.setAppointmentId(1L);
        prescriptionDto.setDoctorId(1L);
        prescriptionDto.setPatientId(1L);
        prescriptionDto.setDiagnosis("kook");
        prescriptionDto.setMedications("vitamine");
        prescriptionDto.setStatus(Prescription.Status.ACTIVE);


    }

    @Test
    void createPrescription_success() {

        when(prescriptionMapper.toEntity(prescriptionDto)).thenReturn(prescription);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(prescriptionRepository.save(prescription)).thenReturn(prescription);
        when(prescriptionMapper.toDto(prescription)).thenReturn(prescriptionDto);

        PrescriptionDto result = prescriptionServiceimpl.createPrescription(prescriptionDto);

        assertThat(result).isNotNull();
    }


    // in case(1) if i put in appointment_id is null because in serviceimpl .i say if appointment_id with null no check that exist id for appointment or not
    //and therefore will become doctor one first in service order
    //in case (2) if i put appointment_id with number and check in test direct for doctor will become error becuase supposdely follow order and here in service will be appointment
//    @Test
//    void createPrescription_doctorNotFound() {
//        when(prescriptionMapper.toEntity(prescriptionDto)).thenReturn(prescription);
//        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
//
//        DoctorNotFoundException exception = assertThrows(DoctorNotFoundException.class, () ->
//                prescriptionServiceimpl.createPrescription(prescriptionDto));
//
////        assertEquals("Not Found Doctor with " +doctor.getId(),exception.getMessage());
//
//    }


    @Test
    void createPrescription_AppointmentNotFound() {
        when(prescriptionMapper.toEntity(prescriptionDto)).thenReturn(prescription);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        AppointmentNotFoundException exception = assertThrows(AppointmentNotFoundException.class, () ->
                prescriptionServiceimpl.createPrescription(prescriptionDto));

        assertEquals("Not Found Appointment with " + appointment.getId(), exception.getMessage());

    }


    @Test
    void test_getPrescriptionById_success() {

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(prescriptionMapper.toDto(prescription)).thenReturn(prescriptionDto);

        PrescriptionDto result = prescriptionServiceimpl.getPrescriptionId(1L);

        assertEquals(result.getDiagnosis(), "kook");
        verify(prescriptionRepository, times(1)).findById(1L);

    }


    @Test
    void test_getPrescriptionById_NotFound() {

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());

        PrescriptionNotFoundException exception = assertThrows(PrescriptionNotFoundException.class, () -> {
            prescriptionServiceimpl.getPrescriptionId(1L);

        });


    }


    @Test
    void test_updatePrescription_success() {

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        doNothing().when(prescriptionMapper).updatePrescriptionFromDto(prescriptionDto, prescription);
        when(prescriptionRepository.save(prescription)).thenReturn(prescription);
        when(prescriptionMapper.toDto(prescription)).thenReturn(prescriptionDto);

        PrescriptionDto result = prescriptionServiceimpl.updatePrescription(1L, prescriptionDto);

        assertEquals(result, prescriptionDto);
        verify(prescriptionMapper, times(1)).updatePrescriptionFromDto(prescriptionDto, prescription);

    }

    @Test
    void test_updatePrescription_notFound() {
        when(prescriptionRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(PrescriptionNotFoundException.class,
                () -> prescriptionServiceimpl.updatePrescription(10L, prescriptionDto));
    }


    @Test
    void test_deletePrescription_success() {

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        prescriptionRepository.save(prescription);
        prescriptionServiceimpl.deletePrescription(1L, Prescription.Status.DELETED);

    }

    @Test
    void getAllByPatientIdAndStatus_success() {

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(prescriptionRepository.findByPatientIdAndStatus(1L, Prescription.Status.ACTIVE)).thenReturn(List.of(prescription));
        when(prescriptionMapper.toDto(prescription)).thenReturn(prescriptionDto);


        List<PrescriptionDto> result = prescriptionServiceimpl.getAllPrescriptionforPatientIdAndStatus(1L, Prescription.Status.ACTIVE);

        assertThat(result).hasSize(1);
        verify(prescriptionRepository).findByPatientIdAndStatus(1L, Prescription.Status.ACTIVE);

    }


    @Test
    void getAllByDoctorIdAndStatus_success() {

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(prescriptionRepository.findByDoctorIdAndStatus(1L, Prescription.Status.ACTIVE)).thenReturn(List.of(prescription));
        when(prescriptionMapper.toDto(prescription)).thenReturn(prescriptionDto);


        List<PrescriptionDto> result = prescriptionServiceimpl.getAllPrescriptionforDoctorIdAndStatus(1L, Prescription.Status.ACTIVE);
        assertThat(result).hasSize(1);
        verify(prescriptionRepository).findByDoctorIdAndStatus(1L, Prescription.Status.ACTIVE);

    }



}


