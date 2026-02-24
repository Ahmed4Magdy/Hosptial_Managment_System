package com.example.demo.service.impl;

import com.example.demo.dto.PaymentDto;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.Payment;
import com.example.demo.exceptionhandler.InvoiceNotFoundException;
import com.example.demo.exceptionhandler.PaymentNotFoundException;
import com.example.demo.mapper.PaymentMapper;
import com.example.demo.repository.InvoiceRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentMapper paymentMapper;


    @Override
    @Transactional
    public PaymentDto createPayment(PaymentDto dto) {
        Invoice invoice = invoiceRepository.findById(dto.getInvoiceId()).orElseThrow(() -> new InvoiceNotFoundException("Not Found invoice with id " + dto.getInvoiceId()));


        if (invoice.getStatus() != Invoice.Status.PENDING) {
            throw new IllegalStateException("Invoice is not payable"); // it's been paid
        }

        Payment payment = paymentMapper.toEntity(dto);
        payment.setInvoice(invoice);
        payment.setPaymentDate(LocalDateTime.now());

        int compare = dto.getAmount().compareTo(invoice.getTotalAmount());


        if (compare > 0) {
            BigDecimal changeAmount = dto.getAmount().subtract(invoice.getTotalAmount());
            payment.setChangeAmount(changeAmount);
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            invoice.setStatus(Invoice.Status.PAID);
            invoiceRepository.save(invoice);

        } else if (compare < 0) {
            payment.setStatus(Payment.PaymentStatus.FAILED);
        } else {
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            invoice.setStatus(Invoice.Status.PAID);
            invoiceRepository.save(invoice);


        }

        Payment savedpayment = paymentRepository.save(payment);
        return paymentMapper.todto(savedpayment);

    }


    public PaymentDto getPaymentByInvoiceId(Long invoiceid) {

        Payment payment = paymentRepository.findByInvoiceId(invoiceid);
        return paymentMapper.todto(payment);

    }


    public PaymentDto updatePayment(Long id, PaymentDto dto) {

        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException("Not Found Payment with " + id));

        Invoice invoice = invoiceRepository.findById(dto.getInvoiceId()).orElseThrow(() -> new InvoiceNotFoundException("Not Found Invoice with " + dto.getInvoiceId()));

        payment.setInvoice(invoice);
        payment.setPaymentDate(dto.getPaymentDate());
        paymentMapper.updatePaymentFromDto(dto, payment);
        Payment saved = paymentRepository.save(payment);
        return paymentMapper.todto(saved);

    }

    public void deletePayment(Long id) {

        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException("Not Found Payment with " + id));

        paymentRepository.deleteById(id);


    }


    public PaymentDto getPaymentWithId(Long id) {

        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException("Not Found Payment with " + id));
        return paymentMapper.todto(payment);
    }

    public List<PaymentDto> getAllPayment() {
        return paymentRepository.findAll().
                stream()
                .map(paymentMapper::todto)
                .collect(Collectors.toList());
    }


}
