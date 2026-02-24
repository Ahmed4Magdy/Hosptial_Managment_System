package com.example.demo.service;

import com.example.demo.dto.InvoiceDto;
import com.example.demo.dto.InvoiceItemDto;
import com.example.demo.dto.InvoiceResponseDto;
import com.example.demo.entity.*;
import com.example.demo.exceptionhandler.InvoiceNotFoundException;
import com.example.demo.mapper.InvoiceItemMapper;
import com.example.demo.mapper.InvoiceMapper;
import com.example.demo.repository.*;
import com.example.demo.service.impl.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {


    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceMapper invoiceMapper;
    @Mock
    private InvoiceItemMapper invoiceItemMapper;
    @Mock
    private InvoiceItemRepository invoiceItemRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceServiceimpl;


    private Doctor doctor;
    private Patient patient;
    private Appointment appointment;
    private InvoiceDto invoiceDto;
    private Invoice invoice;
    private InvoiceItemDto invoiceItemDto;
    private InvoiceItem invoiceItem;

    private InvoiceResponseDto responseDto;

    @BeforeEach
    void setup() {

        doctor = new Doctor();
        doctor.setFullName("ahmed");
        doctor.setEmail("ahmed@gmail.com");
        doctor.setPhoneNumber("019898848");
        doctor.setSpecialization("cardio");


        patient = new Patient();
        patient.setEmail("ahmed@gmail.com");
        patient.setBirthdate(LocalDate.parse("2002-04-14"));


        appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(LocalDateTime.of(2025, 11, 20, 22, 30));


        invoiceDto = new InvoiceDto();
        invoiceDto.setId(1L);
        invoiceDto.setDoctorId(1L);
        invoiceDto.setDoctorName("Dr.Ahmed");
        invoiceDto.setPatientId(1L);
        invoiceDto.setPatientName("Shams");
        invoiceDto.setAppointmentId(1L);
        invoiceDto.setStatus(Invoice.Status.PENDING);


        invoice = new Invoice();
        invoice.setId(1L);
        invoice.setDoctor(doctor);
        invoice.setDoctorName("Dr.Ahmed");
        invoice.setPatient(patient);
        invoice.setPatientName("Shams");
        invoice.setStatus(Invoice.Status.PENDING);


        invoiceItemDto = new InvoiceItemDto();
        invoiceItemDto.setId(1L);
        invoiceItemDto.setInvoiceId(1L);
        invoiceItemDto.setPrice(BigDecimal.valueOf(200));
        invoiceItemDto.setQuantity(4);
        invoiceItemDto.setServiceName("Blood Test");


        invoiceItem = new InvoiceItem();
        invoiceItem.setId(1L);
        invoiceItem.setInvoice(invoice);
        invoiceItem.setPrice(BigDecimal.valueOf(200));
        invoiceItem.setQuantity(4);
        invoiceItem.setServiceName("Blood Test");


        invoiceItemDto = new InvoiceItemDto();
        invoiceItemDto.setId(1L);
        invoiceItemDto.setInvoiceId(1L);
        invoiceItemDto.setPrice(BigDecimal.valueOf(200));
        invoiceItemDto.setQuantity(4);
        invoiceItemDto.setServiceName("Blood Test");
        invoiceItemDto.setTotal(800.00);

        responseDto = new InvoiceResponseDto();


    }


    @Test
    public void test_create_Invoice() {


        invoiceDto.setItems(List.of(invoiceItemDto));


        when(invoiceMapper.toEntity(invoiceDto)).thenReturn(invoice);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(invoiceItemMapper.toEntity(invoiceItemDto)).thenReturn(invoiceItem);
        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(invoiceMapper.toDto(invoice)).thenReturn(responseDto);

        InvoiceResponseDto result = invoiceServiceimpl.createInvoice(invoiceDto);

        assertNotNull(result);


    }


    @Test
    public void test_remove_Invoice() {

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        doNothing().when(invoiceRepository).deleteById(1L);

        invoiceServiceimpl.removeInvoice(1L);

        verify(invoiceRepository, times(1)).deleteById(1L);

    }

    @Test
    public void test_update_Invoice() {


        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        doNothing().when(invoiceMapper).updateInvoiceFromDto(invoiceDto, invoice);

        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(invoiceMapper.toDto(invoice)).thenReturn(responseDto);
        InvoiceResponseDto result = invoiceServiceimpl.updateInvoice(1L, invoiceDto);


    }

    @Test
    public void test_getInvoiceById() {

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(invoiceMapper.toDto(invoice)).thenReturn(responseDto);

        InvoiceResponseDto result = invoiceServiceimpl.getInvoiceById(1L);


    }


    @Test
    public void test_getInvoice() {


        when(invoiceRepository.findAll()).thenReturn(List.of(invoice));
        when(invoiceMapper.toDto(invoice)).thenReturn(responseDto);

        List<InvoiceResponseDto> result = invoiceServiceimpl.getInvoice();
        assertNotNull(result);


    }

    @Test
    public void test_cancelInvoice_Success() {


        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));


        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(invoiceMapper.toDto(invoice)).thenReturn(responseDto);

        InvoiceResponseDto result = invoiceServiceimpl.cancelInvoice(1L);

        assertEquals(Invoice.Status.CANCELED, invoice.getStatus());
        assertNotNull(result);

    }

    @Test
    public void test_cancelInvoice_NotFound_ThrowsException() {


        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        InvoiceNotFoundException exception = assertThrows(InvoiceNotFoundException.class, () -> {
            invoiceServiceimpl.cancelInvoice(1L);
        });


        assertEquals("Not Found invoice", exception.getMessage());


    }

    @Test
    public void test_getInvoiceForPatient() {

        when(patientRepository.existsById(1L)).thenReturn(true);

        when(invoiceRepository.findByPatientId(1L)).thenReturn(List.of(invoice));
        when(invoiceMapper.toDto(invoice)).thenReturn(responseDto);


        List<InvoiceResponseDto> result = invoiceServiceimpl.getInvoicesForPatient(1L);


    }


}