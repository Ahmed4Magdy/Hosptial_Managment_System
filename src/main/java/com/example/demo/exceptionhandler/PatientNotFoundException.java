package com.example.demo.exceptionhandler;

public class PatientNotFoundException  extends RuntimeException{
    public PatientNotFoundException(String message) {
        super(message);
    }
}
