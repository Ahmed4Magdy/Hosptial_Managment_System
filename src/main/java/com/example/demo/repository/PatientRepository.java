package com.example.demo.repository;

import com.example.demo.dto.PatientDto;
import com.example.demo.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

   boolean existsByEmail(String email);
   Optional<Patient> findByEmail(String email);

}
