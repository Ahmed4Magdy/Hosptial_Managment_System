package com.example.demo.service.impl;

import com.example.demo.dto.PrescriptionDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;
import com.example.demo.exceptionhandler.AppointmentNotFoundException;
import com.example.demo.exceptionhandler.DoctorNotFoundException;
import com.example.demo.exceptionhandler.PatientNotFoundException;
import com.example.demo.exceptionhandler.PrescriptionNotFoundException;
import com.example.demo.mapper.PrescriptionMapper;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PrescriptionRepository;
import com.example.demo.service.PrescriptionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, PrescriptionMapper prescriptionMapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.prescriptionMapper = prescriptionMapper;
    }

    @Override
    public PrescriptionDto createPrescription(PrescriptionDto dto) {

        Prescription prescription = prescriptionMapper.toEntity(dto);
        if (dto.getAppointmentId() != null) {
            Appointment existing1 = appointmentRepository.findById(dto.getAppointmentId()).orElseThrow(() ->
                    new AppointmentNotFoundException("Not Found Appointment with " + dto.getAppointmentId()));
            prescription.setAppointment(existing1);

        } else {
            prescription.setAppointment(null);
        }
        Doctor existing2 = doctorRepository.findById(dto.getDoctorId()).orElseThrow(() -> new DoctorNotFoundException("Not Found Doctor with " + dto.getDoctorId()));
        Patient existing3 = patientRepository.findById(dto.getPatientId()).orElseThrow(() -> new PatientNotFoundException("Not Found Patient with " + dto.getPatientId()));

        prescription.setDoctor(existing2);
        prescription.setPatient(existing3);
        prescription.setCreated_at(LocalDateTime.now());
        Prescription saved = prescriptionRepository.save(prescription);

        return prescriptionMapper.toDto(saved);

    }

    public PrescriptionDto updatePrescription(Long id, PrescriptionDto dto) {

        Prescription existing = prescriptionRepository.findById(id).orElseThrow(() -> new PrescriptionNotFoundException("Prescription Not Found With " + id));

        if (dto.getAppointmentId() != null) {
            Appointment existing1 = appointmentRepository.findById(dto.getAppointmentId()).orElseThrow(() -> new AppointmentNotFoundException("Not Found Appointment with " + dto.getAppointmentId()));
            existing.setAppointment(existing1);
        } else {
            existing.setAppointment(null);
        }
        Doctor existing2 = doctorRepository.findById(dto.getDoctorId()).orElseThrow(() -> new DoctorNotFoundException("Not Found Doctor with " + dto.getDoctorId()));
        Patient existing3 = patientRepository.findById(dto.getPatientId()).orElseThrow(() -> new PatientNotFoundException("Not Found Patient with " + dto.getPatientId()));


        existing.setDoctor(existing2);
        existing.setPatient(existing3);
        existing.setUpdated_at(LocalDateTime.now());

        prescriptionMapper.updatePrescriptionFromDto(dto, existing);
        Prescription saved = prescriptionRepository.save(existing);
        return prescriptionMapper.toDto(saved);



    }

    public PrescriptionDto getPrescriptionId(Long id) {

        Prescription existing = prescriptionRepository.findById(id).orElseThrow(() -> new PrescriptionNotFoundException("Prescription Not Found With " + id));

        return prescriptionMapper.toDto(existing);

    }


    public List<PrescriptionDto> getAllprescriptions() {

        List<Prescription> existing = prescriptionRepository.findAll();

        return existing.stream().map(prescriptionMapper::toDto).collect(Collectors.toList());

    }


    public List<PrescriptionDto> getAllPrescriptionforPatientIdAndStatus(Long patientid, Prescription.Status status) {


        Patient patient = patientRepository.findById(patientid).orElseThrow(() -> new PatientNotFoundException("Not Found Patient with" + patientid));

        List<Prescription> prescriptions = prescriptionRepository.findByPatientIdAndStatus(patientid, status);

        return prescriptions.stream().map(prescriptionMapper::toDto).collect(Collectors.toList());

    }


    public List<PrescriptionDto> getAllPrescriptionforDoctorIdAndStatus(Long doctorid, Prescription.Status status) {

        Doctor doctor = doctorRepository.findById(doctorid).orElseThrow(() -> new DoctorNotFoundException("Not Found doctor with" + doctorid));

        List<Prescription> prescriptions = prescriptionRepository.findByDoctorIdAndStatus(doctorid, status);

        return prescriptions.stream().map(prescriptionMapper::toDto).collect(Collectors.toList());

    }


    public List<PrescriptionDto> getAllPrescriptionByStatus(Prescription.Status status) {

        List<Prescription> prescriptions = prescriptionRepository.findByStatus(status);

        return prescriptions.stream().map(prescriptionMapper::toDto).collect(Collectors.toList());

    }


    public void deletePrescription(Long id, Prescription.Status status) {

        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(() -> new PrescriptionNotFoundException("Not Found Prescription with" + id));

        if (status == Prescription.Status.DELETED) {
            prescription.setStatus(Prescription.Status.DELETED);
        }

        prescriptionRepository.save(prescription);

    }


}
