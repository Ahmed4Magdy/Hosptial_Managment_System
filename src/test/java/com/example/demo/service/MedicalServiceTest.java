package com.example.demo.service;

import com.example.demo.dto.MedicalRecordDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;
import com.example.demo.exceptionhandler.PatientNotFoundException;
import com.example.demo.mapper.MedicalMapper;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PrescriptionRepository;
import com.example.demo.service.impl.MedicalServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    //    @Mock
//    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private MedicalMapper medicalMapper;
    @InjectMocks
    private MedicalServiceImpl medicalServiceimpl;


    @Test
    void getMedicalRecord_ShouldThrow_WhenPatientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> medicalServiceimpl.getMedicalRecord(1L));
    }

    @Test
    void getMedicalRecord_ShouldReturnDto() {
        Patient patient = new Patient();
        patient.setId(1L);

        List<Appointment> appointments = List.of(new Appointment());
        List<Prescription> prescriptions = List.of(new Prescription());

        MedicalRecordDto dto = new MedicalRecordDto();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientId(1L)).thenReturn(appointments);
        when(prescriptionRepository.findByPatientId(1L)).thenReturn(prescriptions);
        when(prescriptionRepository.countByPatientId(1L)).thenReturn(5L);
        when(medicalMapper.toResponse(patient, appointments, prescriptions, 5L))
                .thenReturn(dto);

        MedicalRecordDto result = medicalServiceimpl.getMedicalRecord(1L);

        assertNotNull(result);
    }


}
