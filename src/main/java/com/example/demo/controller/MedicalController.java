package com.example.demo.controller;

import com.example.demo.dto.MedicalRecordDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;
import com.example.demo.exceptionhandler.PatientNotFoundException;
import com.example.demo.service.MedicalService;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical")
public class MedicalController {

    private final MedicalService medicalService;

    public MedicalController(MedicalService medicalService) {
        this.medicalService = medicalService;
    }

    @GetMapping("/{patientid}")
    public MedicalRecordDto getMedicalRecord(@PathVariable Long patientid) {

       return medicalService.getMedicalRecord(patientid);


    }

}
