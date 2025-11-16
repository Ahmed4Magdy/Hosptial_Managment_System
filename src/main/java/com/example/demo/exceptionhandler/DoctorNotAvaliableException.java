package com.example.demo.exceptionhandler;

public class DoctorNotAvaliableException extends RuntimeException{

    public DoctorNotAvaliableException(String message){
        super(message);
    }
}
