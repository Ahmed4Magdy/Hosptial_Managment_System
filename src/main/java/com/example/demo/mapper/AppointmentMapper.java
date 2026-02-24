package com.example.demo.mapper;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {


    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "appointmentDateTime", ignore = true)
    Appointment toEntity(AppointmentDto dto);


    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "id",source = "id")
    AppointmentDto toDto(Appointment appointment);


    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "appointmentDateTime", ignore = true)
    void updateAppointmentfromDto(AppointmentDto dto ,@MappingTarget Appointment entity);

    //  يعتبر انت بتعدل علي اوبجكت موجود يعني بتعدل ب دي تي او ل انتيتي ولاكن هيلاقي ان ف اوبجكت زي دوكتور ومريض فهعملهم مانوال حتي لو مش هعملهم ابديت بس اهبقي سايبها احتياطي علشان لو حبيت اعمل ليهم يعني


}
