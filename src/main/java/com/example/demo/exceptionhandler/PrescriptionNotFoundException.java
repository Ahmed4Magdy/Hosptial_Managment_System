package com.example.demo.exceptionhandler;


public class PrescriptionNotFoundException extends RuntimeException {


    public PrescriptionNotFoundException(String message) {
        super(message);
    }

}
