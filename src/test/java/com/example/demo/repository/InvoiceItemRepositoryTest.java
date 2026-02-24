package com.example.demo.repository;

import com.example.demo.entity.Doctor;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.InvoiceItem;
import com.example.demo.entity.Patient;
import com.example.demo.exceptionhandler.InvoiceItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class InvoiceItemRepositoryTest {


    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    private Invoice invoice;

    @BeforeEach
    void setup() {

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


    }

    @Test
    void testSaveAndFindByInvoiceId() {

        invoiceRepository.save(invoice);

        InvoiceItem item1 = new InvoiceItem();
        item1.setInvoice(invoice);
        item1.setServiceName("Blood Test");
        item1.setPrice(BigDecimal.valueOf(100));
        item1.setQuantity(2);
        item1.setTotal(BigDecimal.valueOf(200));
        InvoiceItem saved = invoiceItemRepository.save(item1);


        InvoiceItem items = invoiceItemRepository.findById(saved.getId()).orElseThrow(() -> new InvoiceItemNotFoundException("Not Found InvoiceItem"));

    }

    @Test
    void testDelete() {
        invoiceRepository.save(invoice);

        InvoiceItem item = new InvoiceItem();
        item.setInvoice(invoice);
        item.setServiceName("Blood Test");
        item.setPrice(BigDecimal.valueOf(100));
        item.setQuantity(1);
        item.setTotal(BigDecimal.valueOf(100));
        InvoiceItem saved = invoiceItemRepository.save(item);

        invoiceItemRepository.deleteById(saved.getId());
        assertFalse(invoiceItemRepository.findById(saved.getId()).isPresent());
    }



    @Test
    void testUpdateInvoiceItem() {

        invoiceRepository.save(invoice);

        InvoiceItem item = new InvoiceItem();
        item.setInvoice(invoice);
        item.setServiceName("Blood Test");
        item.setPrice(BigDecimal.valueOf(100));
        item.setQuantity(2);
        item.setTotal(BigDecimal.valueOf(200));
        InvoiceItem savedItem = invoiceItemRepository.save(item);

        // Update quantity
        savedItem.setQuantity(3);
        invoiceItemRepository.save(savedItem);

        InvoiceItem found = invoiceItemRepository.findById(savedItem.getId()).orElseThrow(()-> new InvoiceItemNotFoundException("Not Found InvoiceItem"));
        assertNotNull(found);
        assertEquals(3, found.getQuantity());
    }



}
