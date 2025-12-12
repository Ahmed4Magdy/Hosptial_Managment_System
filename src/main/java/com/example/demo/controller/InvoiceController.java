package com.example.demo.controller;


import com.example.demo.dto.InvoiceDto;
import com.example.demo.dto.InvoiceResponseDto;

import com.example.demo.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {


    private final InvoiceService invoiceService;


    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


    @PostMapping("/add")
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceDto dto) {

        InvoiceResponseDto saved = invoiceService.createInvoice(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }


    @GetMapping("/patientid/{patientId}")
    public List<InvoiceResponseDto> getInvoicesForPatient(@PathVariable Long patientId) {

        return invoiceService.getInvoicesForPatient(patientId);

    }


    @PutMapping("/{invoiceid}")
    public InvoiceResponseDto updateInvoice(@PathVariable Long invoiceid, @RequestBody InvoiceDto dto) {


        return invoiceService.updateInvoice(invoiceid, dto);

    }


    @DeleteMapping("/{invoice}")
    public void removeInvoice(@PathVariable Long invoice) {

        invoiceService.removeInvoice(invoice);

    }

    @GetMapping
    public List<InvoiceResponseDto> getInvoice() {

        return invoiceService.getInvoice();
    }

    @GetMapping("/cancel/{invoiceid}")
    public InvoiceResponseDto cancelInvoice(@PathVariable Long invoiceid) {

        return invoiceService.cancelInvoice(invoiceid);

    }


    @GetMapping("/{id}")
    public InvoiceResponseDto getInvoiceById(@PathVariable Long id) {

        return invoiceService.getInvoiceById(id);


    }


}


