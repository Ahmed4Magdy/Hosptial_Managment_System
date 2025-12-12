package com.example.demo.mapper;

import com.example.demo.dto.InvoiceItemDto;
import com.example.demo.entity.InvoiceItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InvoiceItemMapper {


    @Mapping(target = "total", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    InvoiceItem toEntity(InvoiceItemDto dto);


    @Mapping(target = "invoiceId", source = "invoice.id")
    InvoiceItemDto toDto(InvoiceItem entity);


    @Mapping(target = "id",ignore = true)
    void updateInvoiceItemFromDto(InvoiceItemDto dto, @MappingTarget InvoiceItem entity);


}
