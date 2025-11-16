package com.example.demo.service.impl;

import com.example.demo.dto.DoctorDto;
import com.example.demo.entity.Doctor;
import com.example.demo.exceptionhandler.DoctorNotFoundException;
import com.example.demo.exceptionhandler.DuplicateDoctorException;
import com.example.demo.mapper.DoctorMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.service.DoctorService;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {


    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public DoctorServiceImpl(DoctorRepository doctorRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public DoctorDto createdoctor(DoctorDto dto) {

        if (doctorRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateDoctorException("This email " + dto.getEmail() + " already exist");
        }
        Doctor existing = doctorMapper.toEntity(dto);
        existing.setCreatedAt(LocalDateTime.now());
        Doctor saved = doctorRepository.save(existing);
        return doctorMapper.toDto(saved);


    }

    @Override
    public DoctorDto update(Long id, DoctorDto dto) {

        Doctor existing = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException("Not Found Doctor with " + id));

        doctorMapper.updateDoctorFromDto(dto, existing);

        Doctor saved = doctorRepository.save(existing);

        return doctorMapper.toDto(saved);


    }

    @Override
    public List<DoctorDto> findAllDoctor() {

        return doctorRepository.findAll().stream().map(doctorMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public DoctorDto findDoctorById(Long id) {
        Doctor existing = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException("Not Found Doctor with " + id));

        return doctorMapper.toDto(existing);

    }

    @Override
    public void deleteDoctor(Long id) {

        doctorRepository.deleteById(id);

    }

    @Override
    public DoctorDto findByEmail(String email) {

        Doctor existing = doctorRepository.findByEmail(email).orElseThrow(() -> new DoctorNotFoundException("Not Found " + email + " for Doctor"));
        return doctorMapper.toDto(existing);
    }
}
