package com.example.demo.exceptionhandler;


public class PaymentNotFoundException extends RuntimeException{
    public PaymentNotFoundException(String msg){
        super(msg);
    }
}
