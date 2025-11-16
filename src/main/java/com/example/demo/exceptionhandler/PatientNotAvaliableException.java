package com.example.demo.exceptionhandler;

public class PatientNotAvaliableException extends RuntimeException {

    public PatientNotAvaliableException(String message) {
        super(message);
    }

}
