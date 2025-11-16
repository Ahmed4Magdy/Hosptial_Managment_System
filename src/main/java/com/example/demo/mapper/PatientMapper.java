package com.example.demo.mapper;

import com.example.demo.dto.PatientDto;
import com.example.demo.entity.Patient;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "id", ignore = true)
    Patient toEntity(PatientDto dto);


    PatientDto toDto(Patient patient);

    @Mapping(target = "id", ignore = true)
    void updatepatienttodto(PatientDto dto, @MappingTarget Patient entity);

}
