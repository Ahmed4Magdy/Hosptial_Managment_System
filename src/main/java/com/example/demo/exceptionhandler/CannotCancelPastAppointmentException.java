package com.example.demo.exceptionhandler;

public class CannotCancelPastAppointmentException extends RuntimeException {


    public CannotCancelPastAppointmentException(String message) {
        super(message);
    }

}


