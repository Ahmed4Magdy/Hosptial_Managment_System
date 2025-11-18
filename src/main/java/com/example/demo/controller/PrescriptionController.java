package com.example.demo.controller;

import com.example.demo.dto.PrescriptionDto;
import com.example.demo.entity.Prescription;
import com.example.demo.service.PrescriptionService;
import com.example.demo.service.impl.PrescriptionServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/prescription")
public class PrescriptionController {


    private final PrescriptionService prescriptionService;


    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/add")
    public ResponseEntity<PrescriptionDto> createPrescription(@Valid @RequestBody PrescriptionDto dto) {


        PrescriptionDto saved = prescriptionService.createPrescription(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);


    }


    @PutMapping("/{id}")
    public PrescriptionDto updatePrescription(@PathVariable Long id, @RequestBody @Valid PrescriptionDto dto) {

        return prescriptionService.updatePrescription(id, dto);


    }


    @GetMapping("/{id}")
    public PrescriptionDto getPrescriptionId(@PathVariable Long id) {

        return prescriptionService.getPrescriptionId(id);

    }


    @GetMapping
    public List<PrescriptionDto> getAllprescriptions() {


        return prescriptionService.getAllprescriptions();

    }


    @GetMapping("/allprescriptionforpatient/{patientid}/{status}")
    public List<PrescriptionDto> getAllPrescriptionforPatientIdAndStatus(@PathVariable Long patientid, @PathVariable Prescription.Status status) {

        return prescriptionService.getAllPrescriptionforPatientIdAndStatus(patientid, status);

    }

    @GetMapping("/allprescriptionfordoctor/{doctorid}/{status}")
    public List<PrescriptionDto> getAllPrescriptionforDoctorIdAndStatus(@PathVariable Long doctorid, @PathVariable Prescription.Status status) {

        return prescriptionService.getAllPrescriptionforDoctorIdAndStatus(doctorid, status);

    }


    @GetMapping("/status/{status}")
    public List<PrescriptionDto> getAllPrescriptionByStatus(@PathVariable Prescription.Status status) {


        return prescriptionService.getAllPrescriptionByStatus(status);
    }


    @DeleteMapping("/{id}/{status}")
    public void deletePrescription(@PathVariable Long id, @PathVariable Prescription.Status status) {

        prescriptionService.deletePrescription(id, status);

    }


}
