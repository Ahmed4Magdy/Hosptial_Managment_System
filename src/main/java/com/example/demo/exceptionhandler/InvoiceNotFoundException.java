package com.example.demo.exceptionhandler;


public class InvoiceNotFoundException extends RuntimeException{


    public InvoiceNotFoundException (String message) {

        super(message);

    }


}
