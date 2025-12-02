package com.example.demo.service.impl;

import com.example.demo.dto.AppointmentResponseDto;
import com.example.demo.dto.MedicalRecordDto;
import com.example.demo.dto.PatientDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;
import com.example.demo.exceptionhandler.PatientNotFoundException;
import com.example.demo.mapper.MedicalMapper;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PrescriptionRepository;
import com.example.demo.service.MedicalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalServiceImpl implements MedicalService {

    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalMapper medicalMapper;

    public MedicalServiceImpl(PatientRepository patientRepository, PrescriptionRepository prescriptionRepository, AppointmentRepository appointmentRepository, MedicalMapper medicalMapper) {
        this.patientRepository = patientRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.medicalMapper = medicalMapper;
    }

    @Override
    public MedicalRecordDto getMedicalRecord(Long patientid) {

        Patient patient = patientRepository.findById(patientid).orElseThrow(() -> new PatientNotFoundException("Not Found patient with " + patientid));

        List<Appointment> appointments = appointmentRepository.findByPatientId(patientid);

        long totalPrescription =prescriptionRepository.countByPatientId(patientid);

        List<Prescription> prescriptions = prescriptionRepository.findByPatientId(patientid);

        return medicalMapper.toResponse(patient,appointments,prescriptions,totalPrescription);


    }
}
