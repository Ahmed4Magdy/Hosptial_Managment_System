package com.example.demo.service;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    public AppointmentDto createAppointment(AppointmentDto dto);

    public AppointmentDto cancelAppointment(Long id);

    public AppointmentDto compeleteAppointment(Long id);

    public List<AppointmentDto> getAllDoctorIdAndAppointment(Long doctorid, LocalDate date);

    public List<AppointmentDto> getAllPatientIdAndAppointment(Long patienid, LocalDate date);


    public AppointmentDto getAppointmentById(Long id);

    public List<AppointmentDto> getAllAppointment();

    public void deleteAppointmentById(Long id);

    public AppointmentDto update (Long id,AppointmentDto dto);

    public Page<Appointment> findAll(Pageable pageable);


}

