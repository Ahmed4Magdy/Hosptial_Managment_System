package com.example.demo.controller;

import com.example.demo.dto.DoctorDto;
import com.example.demo.entity.Doctor;
import com.example.demo.exceptionhandler.DoctorNotFoundException;
import com.example.demo.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;


    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


    @PostMapping("/add")
    public ResponseEntity<DoctorDto> createdoctor(@Valid @RequestBody DoctorDto dto) {

        DoctorDto saved = doctorService.createdoctor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    @PutMapping("/{id}")
    public DoctorDto update(@PathVariable Long id,@Valid @RequestBody DoctorDto dto) {

        return doctorService.update(id, dto);
    }

    @GetMapping
    public List<DoctorDto> findAllDoctor() {

        return doctorService.findAllDoctor();
    }

    @GetMapping("/{id}")
    public DoctorDto findDoctorById(@PathVariable Long id) {
        return doctorService.findDoctorById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {

        doctorService.deleteDoctor(id);

    }

    @GetMapping("/email/{email}")
    public DoctorDto findByEmail(@PathVariable String email) {

        return doctorService.findByEmail(email);
    }
}
