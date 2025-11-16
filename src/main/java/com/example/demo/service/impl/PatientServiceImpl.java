package com.example.demo.service.impl;

import com.example.demo.dto.PatientDto;
import com.example.demo.entity.Patient;
import com.example.demo.exceptionhandler.DuplicatePatientException;
import com.example.demo.exceptionhandler.PatientNotFoundException;
import com.example.demo.mapper.PatientMapper;
import com.example.demo.repository.PatientRepository;
import com.example.demo.service.PatientService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {


    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }


    public PatientDto createpatient(PatientDto dto) {

        if (patientRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicatePatientException("this email " + dto.getEmail() + " already exist");
        }

        Patient patient = patientMapper.toEntity(dto);
        patient.setBirthdate(dto.getBirthdate());
        patient.setCreatedAt(LocalDateTime.now());
        Patient saved = patientRepository.save(patient);
        return patientMapper.toDto(saved);

    }

    public PatientDto update(Long id, PatientDto dto) {

        Patient exist = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("not found patient with " + id));
        patientMapper.updatepatienttodto(dto, exist);
        Patient saved = patientRepository.save(exist);
        return patientMapper.toDto(exist);

    }


    public List<PatientDto> findAllPatient() {

        return patientRepository.findAll().stream().map(patientMapper::toDto).collect(Collectors.toList());

    }

    public PatientDto findPatientById(Long id) {

        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("not found patient with " + id));

        return patientMapper.toDto(patient);

    }

    public void delete(Long id) {

        patientRepository.deleteById(id);

    }

    public PatientDto findByEmail(String email) {

        Patient exist = patientRepository.findByEmail(email).orElseThrow(() -> new PatientNotFoundException("not found " + email + " for patient"));

        return patientMapper.toDto(exist);


    }


}
