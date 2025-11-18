package com.example.demo.service;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.dto.PrescriptionDto;
import com.example.demo.entity.Prescription;

import java.util.List;

public interface PrescriptionService {


      public PrescriptionDto createPrescription(PrescriptionDto dto);

      public PrescriptionDto updatePrescription(Long id ,PrescriptionDto dto);

      public PrescriptionDto getPrescriptionId(Long id);

      public List<PrescriptionDto> getAllPrescriptionforPatientIdAndStatus(Long patientid, Prescription.Status status);

      public List<PrescriptionDto> getAllPrescriptionforDoctorIdAndStatus(Long doctorid, Prescription.Status status);

      public List<PrescriptionDto> getAllPrescriptionByStatus(Prescription.Status status);

      public void deletePrescription(Long id,Prescription.Status status);

      public List<PrescriptionDto> getAllprescriptions();

}
