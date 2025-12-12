package com.example.demo.controller;

import com.example.demo.dto.InvoiceItemDto;
import com.example.demo.service.InvoiceItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoiceitem")
public class InvoiceItemController {

    private final InvoiceItemService invoiceItemService;

    public InvoiceItemController(InvoiceItemService invoiceItemService) {
        this.invoiceItemService = invoiceItemService;
    }


    @PutMapping("/{invoiceId}/{invoice}")
    public InvoiceItemDto updateInvoiceItems(@PathVariable Long invoiceId, @PathVariable Long invoice, @RequestBody InvoiceItemDto dto) {

        return invoiceItemService.updateInvoiceItems(invoiceId, invoice, dto);

    }

    @DeleteMapping("/{invoiceitemId}/{invoice}")
    public void removeInvoiceItem(@PathVariable Long invoiceitemId, @PathVariable Long invoice) {

        invoiceItemService.removeInvoiceItem(invoiceitemId, invoice);


    }


    @GetMapping()
    public List<InvoiceItemDto> getInvoice() {

        return invoiceItemService.getInvoiceItem();
    }


    @GetMapping("/{id}")
    public InvoiceItemDto getInvoiceById(@PathVariable Long id) {

        return invoiceItemService.getInvoiceById(id);
    }
}
