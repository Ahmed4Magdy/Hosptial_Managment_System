package com.example.demo.service;

import com.example.demo.dto.PaymentDto;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.Payment;
import com.example.demo.exceptionhandler.InvoiceNotFoundException;
import com.example.demo.exceptionhandler.PaymentNotFoundException;
import com.example.demo.mapper.PaymentMapper;
import com.example.demo.repository.InvoiceRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {


    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private PaymentMapper paymentMapper;


    @InjectMocks
    private PaymentServiceImpl paymentServiceimpl;

    private Invoice invoice;
    private Invoice invoice2;
    private Payment payment;
    private PaymentDto dto;

    @BeforeEach
    void setup() {


        invoice = new Invoice();
        invoice.setId(1L);
        invoice.setDoctorName("ahmed");
        invoice.setPatientName("nour");
        invoice.setStatus(Invoice.Status.PENDING);
        invoice.setTotalAmount(BigDecimal.valueOf(500));

        payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(BigDecimal.valueOf(500));
        payment.setMethod(Payment.PaymentMethod.CASH);
        payment.setPaymentDate(LocalDateTime.now());

        dto = new PaymentDto();
        dto.setInvoiceId(invoice.getId());
        dto.setAmount(BigDecimal.valueOf(500));
        dto.setMethod(Payment.PaymentMethod.CASH);
        dto.setPaymentDate(LocalDateTime.now());


    }


    @Test
    void createPayment_shouldReturnSuccess_withExactAmount() {

        when(invoiceRepository.findById(invoice.getId())).thenReturn(Optional.of(invoice));
        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.todto(payment)).thenReturn(dto);

        PaymentDto result = paymentServiceimpl.createPayment(dto);
        assertNotNull(result);
        assertEquals(Payment.PaymentStatus.SUCCESS, payment.getStatus());
        assertEquals(Invoice.Status.PAID, invoice.getStatus());

    }


    @Test
    void createPayment_shouldReturnSuccess_withChange() {
        dto.setAmount(BigDecimal.valueOf(1000));
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.todto(payment)).thenReturn(dto);

        PaymentDto result = paymentServiceimpl.createPayment(dto);
        assertNotNull(result);
        assertEquals(Payment.PaymentStatus.SUCCESS, payment.getStatus());
        assertEquals(Invoice.Status.PAID, invoice.getStatus());
        assertEquals(BigDecimal.valueOf(500), payment.getChangeAmount());

    }


    @Test
    void createPayment_shouldReturnFailed_WhenAmountLess() {
        dto.setAmount(BigDecimal.valueOf(300));
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.todto(payment)).thenReturn(dto);

        PaymentDto result = paymentServiceimpl.createPayment(dto);
        assertNotNull(result);
        assertEquals(Payment.PaymentStatus.FAILED, payment.getStatus());
        assertEquals(Invoice.Status.PENDING, invoice.getStatus());

    }


    @Test
    void createPayment_shouldThrowException_WhenInvoiceNotFound() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());


        InvoiceNotFoundException exception = assertThrows(InvoiceNotFoundException.class, () -> {
            paymentServiceimpl.createPayment(dto);
        });

        assertEquals("Not Found invoice with id " + dto.getInvoiceId(), exception.getMessage());
    }


    @Test
    void createPayment_shouldThrowIllegalStateException_WhenInvoiceStatusAsPending() {
        invoice.setStatus(Invoice.Status.PAID);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            paymentServiceimpl.createPayment(dto);
        });

        assertEquals("Invoice is not payable", exception.getMessage());
        verify(paymentRepository, never()).save(any());
    }


    @Test
    void getPaymentByInvoiceId_ShouldReturnPayment() {

        when(paymentRepository.findByInvoiceId(1L)).thenReturn(payment);
        when(paymentMapper.todto(payment)).thenReturn(dto);

        PaymentDto result = paymentServiceimpl.getPaymentByInvoiceId(1L);

        assertNotNull(result);
        assertEquals(dto, result);
        verify(paymentRepository, times(1)).findByInvoiceId(1l);
    }


    @Test
    void updatePayment_ShouldReturnSuccess() {

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        doNothing().when(paymentMapper).updatePaymentFromDto(dto, payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.todto(payment)).thenReturn(dto);

        PaymentDto result = paymentServiceimpl.updatePayment(1L, dto);
        assertNotNull(result);
        assertEquals(dto, result);


    }


    @Test
    void updatePayment_ShouldReturnNotFound() {

        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> {
            paymentServiceimpl.updatePayment(1L, dto);
        });

        assertEquals("Not Found Payment with " + 1, exception.getMessage());
        verify(paymentRepository, never()).save(any());
    }


    @Test
    void updatePayment_ShouldReturnNotFound_ForInvoice() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        InvoiceNotFoundException exception = assertThrows(InvoiceNotFoundException.class, () -> {
            paymentServiceimpl.updatePayment(1L, dto);
        });

        assertEquals("Not Found Invoice with " + 1, exception.getMessage());
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void deletePayment_ShouldDeletePayment() {

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        doNothing().when(paymentRepository).deleteById(1L);
        paymentServiceimpl.deletePayment(1L);

        verify(paymentRepository, times(1)).deleteById(1L);

    }


    @Test
    void getPaymentById_ShouldReturnPaymentWithId() {

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentMapper.todto(payment)).thenReturn(dto);

        PaymentDto result = paymentServiceimpl.getPaymentWithId(1L);

        assertEquals(dto, result);
        assertNotNull(result);


    }


    @Test
    void getPaymentById_ShouldThrowException_NotFoundPaymentId() {

        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> {
            paymentServiceimpl.getPaymentWithId(99L);
        });

        assertEquals("Not Found Payment with " + 99, exception.getMessage());
        verify(paymentMapper, never()).todto(any());
    }


    @Test
    void getAllPayment_ShouldReturnAllPayments() {

        Payment payment1;
        payment1 =new Payment();
        payment1.setInvoice(invoice);
        payment1.setAmount(BigDecimal.valueOf(500));
        payment1.setMethod(Payment.PaymentMethod.CASH);
        payment1.setPaymentDate(LocalDateTime.now());

        when(paymentRepository.findAll()).thenReturn(List.of(payment,payment1));
        when(paymentMapper.todto(payment)).thenReturn(dto);

        List<PaymentDto> result = paymentServiceimpl.getAllPayment();
        assertNotNull(result);
//        assertEquals(dto,result);
        assertEquals(2, result.size());
    }


}


//    @Test
//    @DisplayName("Should throw InvoiceNotFoundException when invoice does not exist")
//    void shouldThrowExceptionWhenInvoiceNotFound() {
//        // Given
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // When & Then
//        InvoiceNotFoundException exception = assertThrows(
//                InvoiceNotFoundException.class,
//                () -> paymentServiceimpl.createPayment(dto)
//        );
//
//        assertEquals("Not Found invoice with id 1", exception.getMessage());
//
//        // Verify
//        verify(invoiceRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Should throw IllegalStateException when invoice is already paid")
//    void shouldThrowExceptionWhenInvoiceAlreadyPaid() {
//
//        invoice.setStatus(Invoice.Status.PAID);
//        when(invoiceRepository.findById(invoice.getId())).thenReturn(Optional.of(invoice));
//
//        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
//            paymentServiceimpl.createPayment(dto);
//        });
//
//        assertEquals("Invoice is not payable", exception.getMessage());
//        verify(invoiceRepository, times(1)).findById(invoice.getId());
//
//    }
//
//    @Test
//    @DisplayName("Should throw IllegalStateException when invoice is canceled")
//    void shouldThrowExceptionWhenInvoiceCanceled() {
//        // Given
//        invoice.setStatus(Invoice.Status.CANCELED);
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//
//        // When & Then
//        IllegalStateException exception = assertThrows(
//                IllegalStateException.class,
//                () -> paymentServiceimpl.createPayment(dto)
//        );
//
//        assertEquals("Invoice is not payable", exception.getMessage());
//
//        // Verify
//        verify(invoiceRepository, times(1)).findById(invoice.getId());
//
//    }
//
//    @Test
//    @DisplayName("Should calculate change amount correctly")
//    void shouldCalculateChangeAmountCorrectly() {
//
//        dto.setAmount(BigDecimal.valueOf(750));
//
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//        when(paymentMapper.toEntity(dto)).thenReturn(payment);
//        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
//        when(paymentMapper.todto(payment)).thenReturn(dto);
//
//        PaymentDto result = paymentServiceimpl.createPayment(dto);
//
//        assertEquals(BigDecimal.valueOf(250), payment.getChangeAmount());
//
//
//    }
//
//    @Test
//    @DisplayName("Should create failed payment when amount is zero")
//    void shouldCreateFailedPaymentWhenAmountIsZero() {
//
//        dto.setAmount(BigDecimal.ZERO);
//        payment.setAmount(BigDecimal.ZERO);
//        dto.setStatus(Payment.PaymentStatus.FAILED);
//        payment.setStatus(Payment.PaymentStatus.FAILED);
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//        when(paymentMapper.toEntity(dto)).thenReturn(payment);
//        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
//        when(paymentMapper.todto(payment)).thenReturn(dto);
//
//        PaymentDto result = paymentServiceimpl.createPayment(dto);
//
//        assertEquals(Payment.PaymentStatus.FAILED, result.getStatus());
//
//    }
//
//
//    @Test
//    @DisplayName("Should return payment with InvoiceId")
//    void ShouldReturnPaymentWithInvoiceId() {
//
//        when(paymentRepository.findByInvoiceId(1L)).thenReturn((payment));
//        when(paymentMapper.todto(payment)).thenReturn(dto);
//
//        PaymentDto result = paymentServiceimpl.getPaymentByInvoiceId(1L);
//        verify(paymentRepository, times(1)).findByInvoiceId(1L);
//
//    }
//
//    @Test
//    @DisplayName("Should Update payment")
//    void ShouldUpdatePayment() {
//
//        invoice2 = new Invoice();
//        invoice2.setId(2L);
//        invoice2.setPatient(patient);
//        invoice2.setDoctor(doctor);
//        invoice2.setDoctorName("shams");
//        invoice2.setPatientName("arwa");
//        invoice2.setStatus(Invoice.Status.PENDING);
//        invoice2.setTotalAmount(BigDecimal.valueOf(800));
//        Invoice savedinvoice = invoiceRepository.save(invoice);
//
//        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//
//        payment.setInvoice(invoice2);
//        doNothing().when(paymentMapper).updatePaymentFromDto(dto,payment);
//        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
//        when(paymentMapper.todto(payment)).thenReturn(dto);
//
//
//        PaymentDto result = paymentServiceimpl.updatePayment(1L, dto);
//        assertEquals(Optional.of(2),invoice2.getId());
//
//    }