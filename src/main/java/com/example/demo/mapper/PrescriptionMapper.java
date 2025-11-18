package com.example.demo.mapper;

import com.example.demo.dto.PrescriptionDto;
import com.example.demo.entity.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.awt.*;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {


    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "appointment",ignore = true)
    Prescription toEntity(PrescriptionDto dto);


    @Mapping(target = "doctorId",source = "doctor.id")
    @Mapping(target = "patientId",source = "patient.id")
    @Mapping(target = "appointmentId",source = "appointment.id")
    PrescriptionDto toDto(Prescription entity);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "appointment",ignore = true)
    @Mapping(target = "created_at", ignore = true)  // انا هنا حطيتها علشان مش عايزه يعمل ماتش او بقوله انا هتعامل معاها ولو متعملتش معاها حطها بقيمتها ال هي لو اول مره بي دي فولت زي الانشاء كدا هتبقي ب نل مكنش كدا زي الحاله ال انا فيها دي وهي كان ليها قيمه الاوليه هتبقي زي ما هي
    @Mapping(target = "updated_at", ignore = true)
    void updatePrescriptionFromDto(PrescriptionDto dto , @MappingTarget Prescription entity);

    // هنا انا حطيت اجنور للانشاء والتحديث علشان لما بحط كده معناه اني بمنعه انه يحطه بنل ومعناها ان بقوله انا ال هتصرف فيهم ولو معملتش كده لما هاجي هعدل مانيوال زي ماعملت المابر هيلاقي ان عملت فهيتحكم فيهم لان مش معملهم اجنور فهيبقو ب نل فلاوم اعمل اجنور ليهم

}
