package com.example.demo.mapper;

import com.example.demo.dto.InvoiceDto;
import com.example.demo.dto.InvoiceItemDto;
import com.example.demo.dto.InvoiceResponseDto;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.InvoiceItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",uses = {InvoiceItemMapper.class})
public interface InvoiceMapper {


    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "items",ignore = true)
    Invoice toEntity(InvoiceDto dto);



    @Mapping(target = "doctorId",source = "doctor.id")
    @Mapping(target = "patientId",source = "patient.id")
    @Mapping(target = "appointmentId",source = "appointment.id")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "totalAmount",source = "totalAmount")
    InvoiceResponseDto toDto (Invoice entity);



    @Mapping(target = "id",ignore = true)
    void updateInvoiceFromDto(InvoiceDto dto, @MappingTarget Invoice entity);



}
