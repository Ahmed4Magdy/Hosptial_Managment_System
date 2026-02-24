package com.example.demo.repository;

import com.example.demo.entity.Doctor;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Payment;
import com.example.demo.exceptionhandler.InvoiceNotFoundException;
import com.example.demo.exceptionhandler.PaymentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;


    private Invoice invoice;
    private Payment payment;

    @BeforeEach
    public void setup() {

        Patient patient = new Patient();
        patient.setFullname("John Doe");
        patient.setEmail("ahmed@gmail.com");
        patient.setFullname("ahmed magdy");
        patient.setGender("MALE");
        patient.setPhone("23696238359");
        patient.setBirthdate(LocalDate.of(2002, 4, 14));
        patientRepository.save(patient);

        Doctor doctor = new Doctor();
        doctor.setFullName("Dr. Smith");
        doctor.setEmail("ahmed@gmail.com");
        doctor.setPhoneNumber("00i329802");
        doctor.setSpecialization("cardio");
        doctorRepository.save(doctor);


        invoice = new Invoice();
        invoice.setPatient(patient);
        invoice.setDoctor(doctor);
        invoice.setDoctorName("ahmed");
        invoice.setPatientName("nour");
        invoice.setStatus(Invoice.Status.PENDING);
        invoice.setTotalAmount(BigDecimal.valueOf(100));
        Invoice savedinvoice = invoiceRepository.save(invoice);


        payment = new Payment();


    }

    @Test
    @DisplayName("Should save payment successfully")
    void savePayment_success() {

        invoice = invoiceRepository.findById(invoice.getId()).orElseThrow(() -> new InvoiceNotFoundException("Not Found Invoice"));

        payment.setInvoice(invoice);
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setMethod(Payment.PaymentMethod.CASH);
        payment.setPaymentDate(LocalDateTime.now());

        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        Payment saved = paymentRepository.save(payment);
        invoice.setStatus(Invoice.Status.PAID);
        invoiceRepository.save(invoice);
        assertEquals(Payment.PaymentStatus.SUCCESS, saved.getStatus());


    }

    @Test
    @DisplayName("Should Saved Payment with change amount when overpaid")
    void shouldSavedPaymentWithChangeAmount() {

        payment.setInvoice(invoice);
        payment.setAmount(BigDecimal.valueOf(600));
        payment.setMethod(Payment.PaymentMethod.CASH);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setChangeAmount(BigDecimal.valueOf(100));

        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        Payment saved = paymentRepository.save(payment);
        invoice.setStatus(Invoice.Status.PAID);
        invoiceRepository.save(invoice);
        assertEquals(Payment.PaymentStatus.SUCCESS, saved.getStatus());


    }


    @Test
    @DisplayName("Should Saved Payment as Failed  when amount is less than total")
    void shouldSaveFailedPayment() {

        payment.setInvoice(invoice);
        payment.setAmount(BigDecimal.valueOf(300));
        payment.setMethod(Payment.PaymentMethod.CASH);
        payment.setPaymentDate(LocalDateTime.now());

        payment.setStatus(Payment.PaymentStatus.FAILED);
        Payment saved = paymentRepository.save(payment);
        invoice.setStatus(Invoice.Status.PENDING);
        invoiceRepository.save(invoice);
        assertEquals(Payment.PaymentStatus.FAILED, saved.getStatus());
        assertEquals(new BigDecimal(300), saved.getAmount());


    }

    @Test
    public void ShouldFindPaymentWithInvoiceId() {
        payment.setInvoice(invoice);
        payment.setAmount(BigDecimal.valueOf(600));
        payment.setMethod(Payment.PaymentMethod.CASH);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setChangeAmount(BigDecimal.valueOf(100));

        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        Payment saved = paymentRepository.save(payment);

        Payment payment = paymentRepository.findByInvoiceId(invoice.getId());
        assertEquals(Payment.PaymentStatus.SUCCESS, payment.getStatus());

    }

    @Test
    void ShouldReturnPayment() {
        payment.setInvoice(invoice);
        payment.setAmount(BigDecimal.valueOf(600));
        payment.setMethod(Payment.PaymentMethod.CASH);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setChangeAmount(BigDecimal.valueOf(100));

        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        Payment saved = paymentRepository.save(payment);

        Payment payment = paymentRepository.findById(saved.getId()).orElseThrow(() -> new PaymentNotFoundException("Not Found Payment with " + saved.getId()));
        assertEquals(Payment.PaymentStatus.SUCCESS, payment.getStatus());

    }


    @Test
    void ShouldReturnEmptyWhenPaymentNotFound() {
        payment.setInvoice(invoice);
        payment.setAmount(BigDecimal.valueOf(600));
        payment.setMethod(Payment.PaymentMethod.CASH);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setChangeAmount(BigDecimal.valueOf(100));

        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        Payment saved = paymentRepository.save(payment);

//        Payment found = paymentRepository.findById(999L).orElseThrow(() -> new PaymentNotFoundException("Not Found Payment with " + 999L));
        Optional<Payment> found = paymentRepository.findById(999L);

        assertFalse(found.isPresent());

    }

    @Test
    void ShouldDeletePayment() {
        payment.setInvoice(invoice);
        payment.setAmount(BigDecimal.valueOf(600));
        payment.setMethod(Payment.PaymentMethod.CASH);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setChangeAmount(BigDecimal.valueOf(100));

        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        Payment saved = paymentRepository.save(payment);

        Payment payment = paymentRepository.findById(saved.getId()).orElseThrow(() -> new PaymentNotFoundException("Not Found Payment with " + saved.getId()));

        paymentRepository.deleteById(1L);

    }


    @Test
    void updatePayment() {

        payment.setInvoice(invoice);
        payment.setAmount(BigDecimal.valueOf(600));
        payment.setMethod(Payment.PaymentMethod.CASH);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setChangeAmount(BigDecimal.valueOf(100));

        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        Payment saved1 = paymentRepository.save(payment);


        payment.setInvoice(invoice);
        payment.setAmount(BigDecimal.valueOf(800));
        payment.setMethod(Payment.PaymentMethod.CASH);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setChangeAmount(BigDecimal.valueOf(300));

        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        Payment saved2 = paymentRepository.save(payment);


        Payment payment = paymentRepository.findById(saved1.getId()).orElseThrow(() -> new PaymentNotFoundException("Not Found Payment with " + saved1.getId()));

        Invoice invoice = invoiceRepository.findById(1L).orElseThrow(() -> new InvoiceNotFoundException("Not Found Invoice with " + 1L));

        payment.setAmount(saved2.getAmount());
        payment.setChangeAmount(saved2.getChangeAmount());

        Payment savedafterupdate =paymentRepository.save(payment);

        assertEquals(savedafterupdate.getAmount(),BigDecimal.valueOf(800));


    }

}
