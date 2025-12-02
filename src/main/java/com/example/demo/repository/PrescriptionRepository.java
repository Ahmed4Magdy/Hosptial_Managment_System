package com.example.demo.repository;

import com.example.demo.dto.PrescriptionDto;
import com.example.demo.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {


    List<Prescription> findByPatientIdAndStatus(Long patientid, Prescription.Status status);

    List<Prescription> findByDoctorIdAndStatus(Long doctorid, Prescription.Status status);

    List<Prescription> findByStatus(Prescription.Status status);

    List<Prescription> findByPatientId(Long patientid);

     Long countByPatientId(Long patientid);


}
