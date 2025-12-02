package com.example.demo.repository;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // تبع الحل الاولاني
    boolean existsByDoctorIdAndAppointmentDateTimeAndStatusIn(Long doctorId, LocalDateTime dateTime, List<Appointment.Status> status);

    boolean existsByPatientIdAndAppointmentDateTimeAndStatusIn(Long PatientId, LocalDateTime dateTime, List<Appointment.Status> status);

    // after modification
//    boolean existsByDoctorIdOrPatientIdAndAppointmentDateTimeAndStatusIn(Long doctorId, Long PatientId, LocalDateTime dateTime, List<Appointment.Status> statuses
//    );

    // need appointment related with doctor,time ..it mean's will return all thing
    // such as dr.ahmed and tuesday 2025-11-12 10A.m and 2025-11-12  will return allthing for dr.ahmed with
    // معناهاا ان لو دكتور احمد عنده عشر حالات ف يوم ويوم تاني 5 المفروض لما هحدد دكتور احمد والمواعيد هيرجع كل الداتا الخاصه بيه اسماء المرضي وغيره
    // الخلاصه ده هيرجعلي معاد واحد لان بيبقي ف ثواني ودقايق
//    List<Appointment> findByDoctorIdAndAppointmentDateTime(Long doctorId, LocalDateTime dateTime);
//    List<Appointment> findByPatientIdAndAppointmentDateTime(Long patientId, LocalDateTime dateTime);

    List<Appointment> findByDoctorIdAndAppointmentDateTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);


    List<Appointment> findByPatientIdAndAppointmentDateTimeBetween(Long patientId, LocalDateTime start, LocalDateTime end);


    List<Appointment> findByPatientId(Long patientId);

}
