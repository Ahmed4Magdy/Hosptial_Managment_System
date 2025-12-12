package com.example.demo.repository;

import com.example.demo.entity.Doctor;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.Patient;
import com.example.demo.exceptionhandler.InvoiceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class InvoiceRepositoryTest {


    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    private Invoice invoice;
    private Patient patient;
    private Doctor doctor;

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
    public void test_create_Invoice() {


        Invoice saved = invoiceRepository.save(invoice);


    }


    @Test
    public void test_Find_By_Id() {

        Invoice saved = invoiceRepository.save(invoice);
        Invoice found = invoiceRepository.findById(saved.getId()).orElseThrow(() -> new InvoiceNotFoundException("Not Found Invoice"));
        assertNotNull(found);
        assertEquals(saved.getTotalAmount(), found.getTotalAmount());

    }


    @Test
    public void test_cancel_Invoice() {

        Invoice saved1 = invoiceRepository.save(invoice);

        Invoice invoice = invoiceRepository.findById(saved1.getId()).
                orElseThrow(() -> new InvoiceNotFoundException("Not Found invoice"));

        invoice.setStatus(Invoice.Status.CANCELED);

        Invoice saved2 = invoiceRepository.save(invoice);

        assertNotNull(saved2);


    }


    @Test
    public void test_getInvoice() {

        Invoice saved = invoiceRepository.save(invoice);
        List<Invoice> responseDtos = invoiceRepository.findAll();
        assertNotNull(responseDtos);
        assertThat(responseDtos).hasSize(1);

    }


    @Test
    public void test_FindByPateint_Id() {

        Invoice saved = invoiceRepository.save(invoice);
        List<Invoice> invoice = invoiceRepository.findByPatientId(1L);

        assertNotNull(invoice);
        assertThat(invoice).hasSize(1);

    }


    @Test
    public void test_update_Invoice() {

        Invoice saved = invoiceRepository.save(invoice);

        Invoice existing = invoiceRepository.findById(saved.getId()).
                orElseThrow(() -> new InvoiceNotFoundException("Invoice Not Found "));

        existing.setPatientName("Arwa");

        Invoice saved1 = invoiceRepository.save(existing);

        assertEquals(saved1.getPatientName(),"Arwa");

    }


    @Test
    public void test_removeInvoice(){

        Invoice saved = invoiceRepository.save(invoice);

        Invoice existing = invoiceRepository.findById(saved.getId()).
                orElseThrow(() -> new InvoiceNotFoundException("Invoice Not Found "));

        invoiceRepository.deleteById(existing.getId());


    }


}



