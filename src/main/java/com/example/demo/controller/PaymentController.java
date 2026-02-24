package com.example.demo.controller;

import com.example.demo.dto.PaymentDto;
import com.example.demo.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping("/add")
    public PaymentDto createPayment(@RequestBody PaymentDto dto) {
        return paymentService.createPayment(dto);
    }


    @GetMapping("/invoiceId/{invoiceid}")
    public PaymentDto getPaymentByInvoiceId(@PathVariable Long invoiceid) {

        return paymentService.getPaymentByInvoiceId(invoiceid);
    }


    @PutMapping("/{id}")
    public PaymentDto updatePayment(@PathVariable Long id, @RequestBody PaymentDto dto) {

        return paymentService.updatePayment(id, dto);

    }


    @GetMapping("/{id}")
    public PaymentDto getPaymentWithId(@PathVariable Long id) {

        return paymentService.getPaymentWithId(id);

    }

    @GetMapping()
    public List<PaymentDto> getAllPayment() {
        return paymentService.getAllPayment();

    }


    @DeleteMapping("/{id}")
    public void Delete(@PathVariable Long id) {

        paymentService.deletePayment(id);
    }
}
