package com.example.demo.mapper;

import com.example.demo.dto.DoctorDto;
import com.example.demo.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface DoctorMapper {


    @Mapping(target = "id", ignore = true)
    Doctor toEntity(DoctorDto dto);


    DoctorDto toDto (Doctor doctor);

    @Mapping(target = "id", ignore = true)
    void updateDoctorFromDto(DoctorDto dto , @MappingTarget Doctor entity);
// بتكلم علي اخر واحد ف الانوتيشن انا هنا لما هعمل ابديت علي الانتيتي ..الانتيتي عندي فيها اي دي والدي تي اوو مفيهاش فالبتالي هشلها علشان لما اجي اعمل ماب



}
