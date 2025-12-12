package com.example.demo.service.impl;

import com.example.demo.dto.InvoiceItemDto;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.InvoiceItem;
import com.example.demo.exceptionhandler.InvoiceItemNotFoundException;
import com.example.demo.exceptionhandler.InvoiceNotFoundException;
import com.example.demo.mapper.InvoiceItemMapper;
import com.example.demo.repository.InvoiceItemRepository;
import com.example.demo.repository.InvoiceRepository;
import com.example.demo.service.InvoiceItemService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceItemServiceImpl implements InvoiceItemService {

    private final InvoiceItemMapper invoiceItemMapper;
    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceRepository invoiceRepository;

    public InvoiceItemServiceImpl(InvoiceItemMapper invoiceItemMapper, InvoiceItemRepository invoiceItemRepository, InvoiceRepository invoiceRepository) {
        this.invoiceItemMapper = invoiceItemMapper;
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public InvoiceItemDto createInvoiceItem(InvoiceItemDto dto) {

        InvoiceItem existing = invoiceItemMapper.toEntity(dto);
        existing.setTotal(dto.getPrice() * dto.getQuantity());
        invoiceItemRepository.save(existing);
        return invoiceItemMapper.toDto(existing);

    }


    @Override
    public InvoiceItemDto updateInvoiceItems(Long invoiceitemId, Long invoice, InvoiceItemDto dto) {

        InvoiceItem item = invoiceItemRepository.findById(invoiceitemId).orElseThrow(() -> new InvoiceItemNotFoundException("InvoiceItem Not Found"));
        Invoice existing2 = invoiceRepository.findById(invoice).orElseThrow(() -> new InvoiceNotFoundException("Invoice Not Found"));

        invoiceItemMapper.updateInvoiceItemFromDto(dto, item);


        item.setTotal(item.getPrice() * item.getQuantity());

        Invoice invoice1 = item.getInvoice();

        double total = 0.0;
        for (InvoiceItem i : invoice1.getItems()) {
            total = total + i.getTotal();
        }

        invoice1.setTotalAmount(BigDecimal.valueOf(total));

        Invoice saved = invoiceRepository.save(invoice1);

        return invoiceItemMapper.toDto(item);


    }


    @Override
    public void removeInvoiceItem(Long invoiceitemId, Long invoice) {

        InvoiceItem existing1 = invoiceItemRepository.findById(invoiceitemId).orElseThrow(() -> new InvoiceItemNotFoundException("InvoiceItem Not Found"));
        Invoice exisitng2 = invoiceRepository.findById(invoice).orElseThrow(() -> new InvoiceNotFoundException("Invoice Not Found"));

        invoiceItemRepository.deleteById(invoiceitemId);


    }


    @Override
    public List<InvoiceItemDto> getInvoiceItem() {

        return invoiceItemRepository.findAll().stream().map(invoiceItemMapper::toDto).collect(Collectors.toList());

    }

    @Override
    public InvoiceItemDto getInvoiceById(Long id) {

        InvoiceItem invoiceItem = invoiceItemRepository.findById(id).orElseThrow(() -> new InvoiceItemNotFoundException("Not Found InvoiceItem"));

        return invoiceItemMapper.toDto(invoiceItem);

    }


}
