package com.example.demo.service.impl;

import com.example.demo.dto.InvoiceDto;
import com.example.demo.dto.InvoiceItemDto;
import com.example.demo.dto.InvoiceResponseDto;
import com.example.demo.entity.*;
import com.example.demo.exceptionhandler.DoctorNotFoundException;
import com.example.demo.exceptionhandler.InvoiceNotFoundException;
import com.example.demo.exceptionhandler.PatientNotFoundException;
import com.example.demo.mapper.InvoiceItemMapper;
import com.example.demo.mapper.InvoiceMapper;
import com.example.demo.repository.*;
import com.example.demo.service.InvoiceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    private final InvoiceItemMapper invoiceItemMapper;

    private final InvoiceItemRepository invoiceItemRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    private final AppointmentRepository appointmentRepository;


    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper, InvoiceItemMapper invoiceItemMapper, InvoiceItemRepository invoiceItemRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.invoiceItemMapper = invoiceItemMapper;
        this.invoiceItemRepository = invoiceItemRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }


    // explaination for function
    // الفكره هنا ان شغال كاسكاد علي العناصر فالبتالي لما هعمل حفظ للفاتوره العناصر هتتضاف هي كمان وبعد كده المفروض انا ببعتله البيانات عادي جدا ولما بوصل للعناصر هو اصلا دي تي اوو فبحول للانتيتي وبعدها انا هحط الداتا علشان يتم حفظها بتاعت العناصر وعملت ليست بتاعت العناصر علشان انا نش عارف كام عنصر هيبقي عندي ولما بخلص كل عنصر بضيفه بحطه ف الليسته دي وف نفس الفور اتش دي هو بيحسب مجموع عدد العناصر وبعدها المفروض هيحط الليسته دي ف الفاتوره والتوتال هحطها فالفاتوره لانها تبعها اصلا وبعدها هحفظ ال الفاتوره وبعدها العناصر هتتحفظ تلقاءي علشان الكاسكيد ال عمله والمفروض هرجع دا لدي تي او بقي بالمابر بس منساش ان لما هرجع هرحع الاوبجكت دا جوا اوبجكت تاني بتاع العناصر المفروض هو ليه مابر وبالتالي المفروض طالما في مابر تبع اوبجكت تاني بدخل ف المابر الاساسي وبضيف اسم المابر وبحطه علشان يقدر يعمل ماتش ويرجع قيمه صح
    @Transactional
    public InvoiceResponseDto createInvoice(InvoiceDto dto) {

        // 1️⃣ mapping الأساسيات
        Invoice invoice = invoiceMapper.toEntity(dto);
        invoice.setStatus(Invoice.Status.PENDING);
        invoice.setUpdated_at(null);


        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Not Found Doctor with " + dto.getDoctorId()));
        invoice.setDoctor(doctor);


        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Not Found Patient with " + dto.getPatientId()));
        invoice.setPatient(patient);


        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        invoice.setAppointment(appointment);


        BigDecimal totalAmount = BigDecimal.ZERO;
        List<InvoiceItem> items = new ArrayList<>();

        for (InvoiceItemDto itemDto : dto.getItems()) {

            InvoiceItem item = invoiceItemMapper.toEntity(itemDto);

            item.setInvoice(invoice);
            item.setTotal(item.getPrice() * item.getQuantity());
            items.add(item);


            totalAmount = totalAmount.add(BigDecimal.valueOf(item.getTotal()));

        }

        invoice.setItems(items);
        invoice.setTotalAmount(totalAmount);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        return invoiceMapper.toDto(savedInvoice);
    }


    public void removeInvoice(Long invoice) {

        Invoice exisitng = invoiceRepository.findById(invoice).orElseThrow(() -> new InvoiceNotFoundException("Invoice Not Found"));
        invoiceRepository.deleteById(invoice);


    }

    @Override
    public InvoiceResponseDto updateInvoice(Long invoiceid, InvoiceDto dto) {

        Invoice existing = invoiceRepository.findById(invoiceid).
                orElseThrow(() -> new InvoiceNotFoundException("Invoice Not Found "));

        invoiceMapper.updateInvoiceFromDto(dto, existing);
        existing.setUpdated_at(LocalDateTime.now());
        Invoice saved = invoiceRepository.save(existing);
        return invoiceMapper.toDto(saved);


    }

    @Override
    public List<InvoiceResponseDto> getInvoicesForPatient(Long patientId) {

        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Not Found Patient");
        }

        List<Invoice> invoice = invoiceRepository.findByPatientId(patientId);
        return invoice.stream().map(invoiceMapper::toDto).collect(Collectors.toList());

    }

    @Override
    public List<InvoiceResponseDto> getInvoice() {

        return invoiceRepository.findAll().stream().map(invoiceMapper::toDto).collect(Collectors.toList());

    }

    @Override
    public InvoiceResponseDto cancelInvoice(Long invoiceid) {

        Invoice invoice = invoiceRepository.findById(invoiceid).
                orElseThrow(() -> new InvoiceNotFoundException("Not Found invoice"));

        if (invoice.getStatus() != Invoice.Status.PENDING) {
            throw new IllegalStateException("Only PENDING invoices can be cancelled");

        }
        invoice.setStatus(Invoice.Status.CANCELED);

        Invoice saved = invoiceRepository.save(invoice);

        return invoiceMapper.toDto(invoice);

    }

    @Override
    public InvoiceResponseDto getInvoiceById(Long id) {

        Invoice invoice = invoiceRepository.findById(id).
                orElseThrow(() -> new InvoiceNotFoundException("Not Found Invoice"));

        return invoiceMapper.toDto(invoice);


    }


}