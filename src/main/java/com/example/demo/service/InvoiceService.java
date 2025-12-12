package com.example.demo.service;


import com.example.demo.dto.InvoiceDto;
import com.example.demo.dto.InvoiceResponseDto;

import java.util.List;


public interface InvoiceService {


    InvoiceResponseDto createInvoice(InvoiceDto dto);

//   void removeInvoice(Long invoice, Invoice.Status status);

    void removeInvoice(Long invoice);

    InvoiceResponseDto updateInvoice(Long invoiceid, InvoiceDto dto);


    List<InvoiceResponseDto> getInvoicesForPatient(Long patientId);


    List<InvoiceResponseDto> getInvoice();


    InvoiceResponseDto cancelInvoice(Long invoiceid);


    InvoiceResponseDto getInvoiceById(Long id);


}