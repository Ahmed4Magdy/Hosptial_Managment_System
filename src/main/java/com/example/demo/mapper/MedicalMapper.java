package com.example.demo.mapper;

import com.example.demo.dto.AppointmentResponseDto;
import com.example.demo.dto.MedicalRecordDto;
import com.example.demo.dto.PrescriptionResponseDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = {DoctorMapper.class})
public interface MedicalMapper {

    List<PrescriptionResponseDto> toPrescriptionDto(List<Prescription> prescriptions);

    List<AppointmentResponseDto> toAppointmentDto(List<Appointment> appointments);


    @Mapping(target = "id", source = "patient.id")
    @Mapping(target = "fullname", source = "patient.fullname")
    @Mapping(target = "appointments", source = "appointments")
    @Mapping(target = "prescriptions", source = "prescriptions")
    @Mapping(target = "totalPrescription", source = "totalPrescription")
    MedicalRecordDto toResponse(
            Patient patient,
            List<Appointment> appointments,
            List<Prescription> prescriptions,
            long totalPrescription );
}
