package com.example.demo.service;


import com.example.demo.dto.InvoiceItemDto;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.InvoiceItem;
import com.example.demo.mapper.InvoiceItemMapper;
import com.example.demo.repository.InvoiceItemRepository;
import com.example.demo.repository.InvoiceRepository;
import com.example.demo.service.impl.InvoiceItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceItemServiceTest {


    @Mock
    private InvoiceItemMapper invoiceItemMapper;
    @Mock
    private InvoiceItemRepository invoiceItemRepository;
    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceItemServiceImpl invoiceItemService;


    private Invoice invoice;
    private InvoiceItemDto invoiceItemDto;
    private InvoiceItem invoiceItem;


    @BeforeEach
    void setup() {

        invoice = new Invoice();
        invoice.setId(1L);
        invoice.setDoctorName("Dr.Ahmed");
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


    }


    @Test
    public void test_createInvoiceItem() {


        when(invoiceItemMapper.toEntity(invoiceItemDto)).thenReturn(invoiceItem);
        when(invoiceItemRepository.save(invoiceItem)).thenReturn(invoiceItem);
        when(invoiceItemMapper.toDto(invoiceItem)).thenReturn(invoiceItemDto);

        InvoiceItemDto result = invoiceItemService.createInvoiceItem(invoiceItemDto);


    }


    @Test
    public void test_update_InvoiceItem() {


        when(invoiceItemRepository.findById(1L)).thenReturn(Optional.of(invoiceItem));
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        doNothing().when(invoiceItemMapper).updateInvoiceItemFromDto(invoiceItemDto, invoiceItem);
        Invoice invoice1 = invoiceItem.getInvoice();
        when(invoiceRepository.save(invoice1)).thenReturn(invoice1);
        when(invoiceItemMapper.toDto(invoiceItem)).thenReturn(invoiceItemDto);


        InvoiceItemDto result = invoiceItemService.updateInvoiceItems(1L, 1L, invoiceItemDto);

    }


    @Test
    public void test_removeInvoiceItem() {


        when(invoiceItemRepository.findById(1L)).thenReturn(Optional.of(invoiceItem));
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        doNothing().when(invoiceItemRepository).deleteById(1L);

        invoiceItemService.removeInvoiceItem(1L, 1L);

        verify(invoiceItemRepository, times(1)).deleteById(1L);


    }

    @Test
    public void test_getInvoice() {

        when(invoiceItemRepository.findAll()).thenReturn(List.of(invoiceItem));
        when(invoiceItemMapper.toDto(invoiceItem)).thenReturn(invoiceItemDto);
        List<InvoiceItemDto> result = invoiceItemService.getInvoiceItem();

    }

    @Test
    public void test_getInvoiceId() {

        when(invoiceItemRepository.findById(1L)).thenReturn(Optional.of(invoiceItem));

        when(invoiceItemMapper.toDto(invoiceItem)).thenReturn(invoiceItemDto);

        InvoiceItemDto result = invoiceItemService.getInvoiceById(1L);

    }


}
