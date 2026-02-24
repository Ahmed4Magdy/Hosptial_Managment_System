package com.example.demo.service;

import com.example.demo.dto.PaymentDto;

import java.util.List;

public interface PaymentService {


    PaymentDto createPayment(PaymentDto dto);

    PaymentDto getPaymentByInvoiceId(Long invoiceid);

    PaymentDto updatePayment(Long id, PaymentDto paymentDto);

    void deletePayment(Long id);

    PaymentDto getPaymentWithId(Long id);

    List<PaymentDto> getAllPayment();
}
