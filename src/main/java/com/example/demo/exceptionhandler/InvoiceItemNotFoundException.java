package com.example.demo.exceptionhandler;

public class InvoiceItemNotFoundException extends RuntimeException {

    public InvoiceItemNotFoundException(String message) {

        super(message);

    }

}
