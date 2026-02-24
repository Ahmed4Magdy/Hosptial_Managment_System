package com.example.demo.mapper;

import com.example.demo.dto.PaymentDto;
import com.example.demo.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "invoice", ignore = true)
    Payment toEntity(PaymentDto dto);

    @Mapping(target = "invoiceId", source = "invoice.id")
    PaymentDto todto(Payment entity);


    @Mapping(target = "invoice",ignore = true)
    @Mapping(target = "id",ignore = true)
    void updatePaymentFromDto (PaymentDto dto , @MappingTarget Payment entity);

}
