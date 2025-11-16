package com.example.demo.controller;

import com.example.demo.dto.PatientDto;
import com.example.demo.entity.Patient;
import com.example.demo.mapper.PatientMapper;
import com.example.demo.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    public PatientController(PatientService patientService, PatientMapper patientMapper) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
    }


    @PostMapping("/add")
    public PatientDto createpatient(@RequestBody @Valid PatientDto dto) {


        return patientService.createpatient(dto);

    }

    @PutMapping("/{id}")
    public PatientDto update(@PathVariable Long id,@RequestBody @Valid PatientDto dto) {


        return patientService.update(id, dto);

    }


    @GetMapping("")
    public List<PatientDto> findAllPatient() {

        return patientService.findAllPatient();

    }

    @GetMapping("/{id}")
    public PatientDto findPatientById(@PathVariable Long id) {

        return patientService.findPatientById(id);

    }


    @DeleteMapping("/{id}")
    public void delete (@PathVariable Long id){

        patientService.delete(id);

    }


    @GetMapping("/email/{email}")
    public PatientDto findByEmail(@PathVariable String email) {


        return patientService.findByEmail(email);


    }


}
