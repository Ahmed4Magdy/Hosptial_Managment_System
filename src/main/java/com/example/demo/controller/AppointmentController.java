package com.example.demo.controller;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.entity.Appointment;
import com.example.demo.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {


    private final AppointmentService appointmentService;


    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    @PostMapping("/add")
    public ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentDto dto) {

        AppointmentDto saved = appointmentService.createAppointment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);


    }


    @PutMapping("/update/{id}")
    public AppointmentDto update(@PathVariable Long id, @Valid @RequestBody AppointmentDto dto) {

        return appointmentService.update(id, dto);
    }


    @GetMapping("/cancel/{id}")
    public AppointmentDto cancelAppointment(@PathVariable Long id) {

        return appointmentService.cancelAppointment(id);


    }


    @GetMapping("/complete/{id}")
    public AppointmentDto compeleteAppointment(@PathVariable Long id) {

        return appointmentService.compeleteAppointment(id);


    }


    @GetMapping("/getdoctorData/{doctorid}")
    public List<AppointmentDto> getAllDoctorIdAndAppointment(@PathVariable Long doctorid, @RequestParam LocalDate date) {

        return appointmentService.getAllDoctorIdAndAppointment(doctorid, date);

    }

    @GetMapping("/getpatientData/{patientid}")
    public List<AppointmentDto> getAllPatientIdAndAppointment(@PathVariable Long patientid, @RequestParam LocalDate date) {

        return appointmentService.getAllPatientIdAndAppointment(patientid, date);
    }


    @GetMapping("/getappointmentById/{id}")
    public AppointmentDto getAppointmentById(@PathVariable Long id) {

        return appointmentService.getAppointmentById(id);
    }


    @GetMapping
    public List<AppointmentDto> getAllAppointment() {

        return appointmentService.getAllAppointment();
    }


    @GetMapping("/pageable")
    public Page<Appointment> getAllAppointments(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int size,
                                                @RequestParam(defaultValue = "id") String sortBy,
                                                @RequestParam(defaultValue = "true") boolean ascending){
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return appointmentService.findAll(pageable);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointmentById(@PathVariable Long id) {

        appointmentService.deleteAppointmentById(id);
    }

}

