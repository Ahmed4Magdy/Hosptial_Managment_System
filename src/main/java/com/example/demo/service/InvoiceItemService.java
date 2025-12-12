package com.example.demo.service;
import com.example.demo.dto.InvoiceItemDto;
import java.util.List;


public interface InvoiceItemService {


    InvoiceItemDto createInvoiceItem(InvoiceItemDto dto);

    InvoiceItemDto updateInvoiceItems(Long invoiceId, Long invoice, InvoiceItemDto dto);

    void removeInvoiceItem(Long invoiceitemId, Long invoice);

    List<InvoiceItemDto> getInvoiceItem();

    InvoiceItemDto getInvoiceById(Long id);


}
