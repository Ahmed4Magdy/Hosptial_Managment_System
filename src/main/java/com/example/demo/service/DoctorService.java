package com.example.demo.service;

import com.example.demo.dto.DoctorDto;
import java.util.List;

public interface DoctorService {


    public DoctorDto createdoctor(DoctorDto dto);

    public DoctorDto update(Long id, DoctorDto dto);

    public List<DoctorDto> findAllDoctor();

    public DoctorDto findDoctorById(Long id);

    public void deleteDoctor(Long id);

    public DoctorDto findByEmail(String email);

}
